package cl.orioneta.auth.infrastructure.web;

import cl.orioneta.auth.app.dto.*;
import cl.orioneta.auth.app.service.AuthService;
import cl.orioneta.auth.app.service.PasswordResetService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST publica del auth-service.
 *
 * <p>Los endpoints de email/password entregan inmediatamente tokens propios de
 * Orioneta. Para Google/GitHub el frontend redirige al usuario a las URLs
 * devueltas por {@link #providers()}, y el success handler completa la sesion
 * cuando el proveedor externo responde.</p>
 *
 * <p>Arquitectura JWT:
 * - Endpoints bajo /api/v1/auth/** son públicos
 * - Genera tokens JWT con claims: userId, email, roles
 * - Usa BCrypt para encriptación de contraseñas
 * - Sesión stateless con tokens JWT</p>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService authService, PasswordResetService passwordResetService) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
    }
    /**
     * Registra una cuenta local y devuelve una sesion inicial.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    /**
     * Inicia sesion con email y password.
     */
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    /**
     * Renueva la sesion usando refresh token opaco.
     */
    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refresh(request);
    }

    /**
     * Valida un access token propio de Orioneta.
     */
    @PostMapping("/validate")
    public TokenValidationResponse validate(@Valid @RequestBody ValidateTokenRequest request) {
        return authService.validateAccessToken(request.accessToken());
    }

    /**
     * Devuelve las rutas que inician login OAuth2 con proveedores soportados.
     */
    @GetMapping("/oauth2/providers")
    public List<OAuth2ProviderResponse> providers() {
        return List.of(
                new OAuth2ProviderResponse("google", "/oauth2/authorization/google"),
                new OAuth2ProviderResponse("github", "/oauth2/authorization/github")
        );
    }

    /**
     * Recupera la contraseña un codigo enviado mediante el correo de la cuenta.
     */
    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public MessageResponse forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        passwordResetService.requestReset(request.email());
        return new MessageResponse("Código de recuperación enviado al correo registrado.");
    }

    @PostMapping("/verify-reset-code")
    public MessageResponse verifyResetCode(@Valid @RequestBody VerifyResetCodeRequest request) {
        passwordResetService.verifyCode(request.email(), request.code());
        return new MessageResponse("Código verificado correctamente.");
    }

    @PostMapping("/reset-password")
    public MessageResponse resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.email(), request.token(), request.newPassword());
        return new MessageResponse("Contraseña actualizada correctamente.");
    }
}
