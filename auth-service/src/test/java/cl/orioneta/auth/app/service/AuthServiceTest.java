package cl.orioneta.auth.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.orioneta.auth.app.dto.AuthResponse;
import cl.orioneta.auth.app.dto.LoginRequest;
import cl.orioneta.auth.app.dto.OAuth2Profile;
import cl.orioneta.auth.app.dto.RefreshTokenRequest;
import cl.orioneta.auth.app.dto.RegisterRequest;
import cl.orioneta.auth.app.repository.AuthUserRepository;
import cl.orioneta.auth.app.repository.RefreshTokenRepository;
import cl.orioneta.auth.app.security.AccessToken;
import cl.orioneta.auth.app.security.PasswordHasher;
import cl.orioneta.auth.app.security.TokenManager;
import cl.orioneta.auth.domain.exception.InvalidCredentialsException;
import cl.orioneta.auth.domain.exception.InvalidRefreshTokenException;
import cl.orioneta.auth.domain.model.AuthProvider;
import cl.orioneta.auth.domain.model.AuthUser;
import cl.orioneta.auth.domain.model.RefreshToken;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Pruebas unitarias de la orquestacion principal de autenticacion.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthUserRepository authUserRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private TokenManager tokenManager;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(authUserRepository, refreshTokenRepository, passwordHasher, tokenManager);
    }

    @Test
    void registerCreatesLocalUserAndReturnsOwnSession() {
        RegisterRequest request = new RegisterRequest("ORI@ORIONETA.CL", "password-segura", "Ori");
        when(authUserRepository.existsByEmail("ori@orioneta.cl")).thenReturn(false);
        when(passwordHasher.hash("password-segura")).thenReturn("hashed-password");
        when(authUserRepository.save(any(AuthUser.class))).thenAnswer(invocation -> invocation.getArgument(0));
        stubSession();

        AuthResponse response = authService.register(request);

        ArgumentCaptor<AuthUser> userCaptor = ArgumentCaptor.forClass(AuthUser.class);
        verify(authUserRepository).save(userCaptor.capture());

        assertThat(userCaptor.getValue().getEmail()).isEqualTo("ori@orioneta.cl");
        assertThat(userCaptor.getValue().getProvider()).isEqualTo(AuthProvider.EMAIL);
        assertThat(userCaptor.getValue().getProviderUserId()).isEqualTo("ori@orioneta.cl");
        assertThat(userCaptor.getValue().hasPassword()).isTrue();
        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
    }

    @Test
    void loginRejectsInvalidPasswordWithoutIssuingTokens() {
        AuthUser user = AuthUser.createLocal("ori@orioneta.cl", "hashed-password", "Ori");
        LoginRequest request = new LoginRequest("ori@orioneta.cl", "mala-password");

        when(authUserRepository.findByEmail("ori@orioneta.cl")).thenReturn(Optional.of(user));
        when(passwordHasher.matches("mala-password", "hashed-password")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Credenciales invalidas");

        verify(tokenManager, never()).createAccessToken(any(AuthUser.class));
        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
    }

    @Test
    void refreshRotatesRefreshTokenAndIssuesNewSession() {
        AuthUser user = AuthUser.createLocal("ori@orioneta.cl", "hashed-password", "Ori");
        RefreshToken oldRefreshToken = RefreshToken.create(
                user.getId(),
                "old-refresh-hash",
                LocalDateTime.now().plusDays(1)
        );

        when(tokenManager.hashRefreshToken("old-refresh-token")).thenReturn("old-refresh-hash");
        when(refreshTokenRepository.findByTokenHash("old-refresh-hash")).thenReturn(Optional.of(oldRefreshToken));
        when(authUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        stubSession();

        AuthResponse response = authService.refresh(new RefreshTokenRequest("old-refresh-token"));

        assertThat(oldRefreshToken.getRevokedAt()).isNotNull();
        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
        verify(refreshTokenRepository).findByTokenHash("old-refresh-hash");
    }

    @Test
    void refreshRejectsRevokedToken() {
        AuthUser user = AuthUser.createLocal("ori@orioneta.cl", "hashed-password", "Ori");
        RefreshToken revokedRefreshToken = RefreshToken.create(
                user.getId(),
                "old-refresh-hash",
                LocalDateTime.now().plusDays(1)
        );
        revokedRefreshToken.revoke();

        when(tokenManager.hashRefreshToken("old-refresh-token")).thenReturn("old-refresh-hash");
        when(refreshTokenRepository.findByTokenHash("old-refresh-hash")).thenReturn(Optional.of(revokedRefreshToken));

        assertThatThrownBy(() -> authService.refresh(new RefreshTokenRequest("old-refresh-token")))
                .isInstanceOf(InvalidRefreshTokenException.class)
                .hasMessage("Refresh token expirado o revocado");
    }

    @Test
    void oauthLoginCreatesUserAndReturnsOwnSession() {
        OAuth2Profile profile = new OAuth2Profile(
                AuthProvider.GOOGLE,
                "google-123",
                "ori@orioneta.cl",
                "Ori",
                "https://cdn.orioneta.cl/avatar.png"
        );

        when(authUserRepository.findByProviderAndProviderUserId(AuthProvider.GOOGLE, "google-123")).thenReturn(Optional.empty());
        when(authUserRepository.findByEmail("ori@orioneta.cl")).thenReturn(Optional.empty());
        when(authUserRepository.save(any(AuthUser.class))).thenAnswer(invocation -> invocation.getArgument(0));
        stubSession();

        AuthResponse response = authService.loginWithOAuth2(profile);

        ArgumentCaptor<AuthUser> userCaptor = ArgumentCaptor.forClass(AuthUser.class);
        verify(authUserRepository).save(userCaptor.capture());

        assertThat(userCaptor.getValue().getProvider()).isEqualTo(AuthProvider.GOOGLE);
        assertThat(userCaptor.getValue().getProviderUserId()).isEqualTo("google-123");
        assertThat(userCaptor.getValue().hasPassword()).isFalse();
        assertThat(response.accessToken()).isEqualTo("access-token");
    }

    private void stubSession() {
        when(tokenManager.createAccessToken(any(AuthUser.class)))
                .thenReturn(new AccessToken("access-token", Instant.now().plusSeconds(900), 900));
        when(tokenManager.createRefreshToken()).thenReturn("refresh-token");
        when(tokenManager.hashRefreshToken("refresh-token")).thenReturn("refresh-token-hash");
        when(tokenManager.refreshTokenExpiresAt()).thenReturn(LocalDateTime.now().plusDays(30));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }
}
