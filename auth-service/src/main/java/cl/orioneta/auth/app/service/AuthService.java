package cl.orioneta.auth.app.service;

import cl.orioneta.auth.app.dto.AuthResponse;
import cl.orioneta.auth.app.dto.LoginRequest;
import cl.orioneta.auth.app.dto.OAuth2Profile;
import cl.orioneta.auth.app.dto.RefreshTokenRequest;
import cl.orioneta.auth.app.dto.RegisterRequest;
import cl.orioneta.auth.app.dto.TokenValidationResponse;
import cl.orioneta.auth.app.repository.AuthUserRepository;
import cl.orioneta.auth.app.repository.RefreshTokenRepository;
import cl.orioneta.auth.app.security.AccessToken;
import cl.orioneta.auth.app.security.PasswordHasher;
import cl.orioneta.auth.app.security.TokenClaims;
import cl.orioneta.auth.app.security.TokenManager;
import cl.orioneta.auth.domain.exception.AuthUserAlreadyExistsException;
import cl.orioneta.auth.domain.exception.AuthUserNotFoundException;
import cl.orioneta.auth.domain.exception.InvalidCredentialsException;
import cl.orioneta.auth.domain.exception.InvalidRefreshTokenException;
import cl.orioneta.auth.domain.model.AuthProvider;
import cl.orioneta.auth.domain.model.AuthUser;
import cl.orioneta.auth.domain.model.RefreshToken;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio principal de autenticacion.
 *
 * <p>La idea central es que Google/GitHub solo prueban identidad externa. Las
 * sesiones internas se manejan con access tokens JWT y refresh tokens emitidos
 * por Orioneta.</p>
 */
@Service
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordHasher passwordHasher;
    private final TokenManager tokenManager;

    public AuthService(
            AuthUserRepository authUserRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordHasher passwordHasher,
            TokenManager tokenManager
    ) {
        this.authUserRepository = authUserRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordHasher = passwordHasher;
        this.tokenManager = tokenManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = requireEmail(request.email());

        if (authUserRepository.existsByEmail(email)) {
            throw new AuthUserAlreadyExistsException("El email ya esta registrado");
        }

        AuthUser user = AuthUser.createLocal(email, passwordHasher.hash(request.password()), request.displayName());
        AuthUser savedUser = authUserRepository.save(user);

        return issueSession(savedUser);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        AuthUser user = authUserRepository.findByEmail(requireEmail(request.email()))
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales invalidas"));

        ensureEnabled(user);

        if (!user.hasPassword() || !passwordHasher.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Credenciales invalidas");
        }

        return issueSession(user);
    }

    @Transactional
    public AuthResponse loginWithOAuth2(OAuth2Profile profile) {
        String email = requireEmail(profile.email());

        AuthUser user = authUserRepository.findByProviderAndProviderUserId(profile.provider(), profile.providerUserId())
                .or(() -> authUserRepository.findByEmail(email))
                .map(existingUser -> linkProviderIfNeeded(existingUser, profile))
                .orElseGet(() -> AuthUser.createOAuth(
                        profile.provider(),
                        profile.providerUserId(),
                        email,
                        profile.displayName(),
                        profile.avatarUrl()
                ));

        ensureEnabled(user);

        AuthUser savedUser = authUserRepository.save(user);
        return issueSession(savedUser);
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        String tokenHash = tokenManager.hashRefreshToken(request.refreshToken());
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token invalido"));

        if (!refreshToken.isActive()) {
            throw new InvalidRefreshTokenException("Refresh token expirado o revocado");
        }

        AuthUser user = authUserRepository.findById(refreshToken.getAuthUserId())
                .orElseThrow(() -> new AuthUserNotFoundException("Usuario auth no encontrado"));

        ensureEnabled(user);

        refreshToken.revoke();
        refreshTokenRepository.save(refreshToken);

        return issueSession(user);
    }

    @Transactional(readOnly = true)
    public TokenValidationResponse validateAccessToken(String accessToken) {
        TokenClaims claims = tokenManager.validateAccessToken(accessToken);

        return new TokenValidationResponse(true, claims.userId(), claims.email(), claims.role(), claims.expiresAt());
    }

    private AuthResponse issueSession(AuthUser user) {
        AccessToken accessToken = tokenManager.createAccessToken(user);
        String rawRefreshToken = tokenManager.createRefreshToken();
        String refreshTokenHash = tokenManager.hashRefreshToken(rawRefreshToken);

        refreshTokenRepository.save(RefreshToken.create(user.getId(), refreshTokenHash, tokenManager.refreshTokenExpiresAt()));

        return new AuthResponse(
                accessToken.value(),
                rawRefreshToken,
                "Bearer",
                accessToken.expiresInSeconds(),
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }

    private AuthUser linkProviderIfNeeded(AuthUser user, OAuth2Profile profile) {
        if (profile.provider() == AuthProvider.EMAIL) {
            return user;
        }

        if (user.getProvider() == AuthProvider.EMAIL || user.getProviderUserId().isBlank()) {
            user.linkOAuthProvider(profile.provider(), profile.providerUserId(), profile.avatarUrl());
        }

        return user;
    }

    private void ensureEnabled(AuthUser user) {
        if (!user.isEnabled()) {
            throw new InvalidCredentialsException("La cuenta esta deshabilitada");
        }
    }

    private String requireEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        return email.trim().toLowerCase(Locale.ROOT);
    }
}
