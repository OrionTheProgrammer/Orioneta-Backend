package cl.orioneta.auth.infrastructure.web;

import cl.orioneta.auth.app.dto.AuthResponse;
import cl.orioneta.auth.app.dto.LoginRequest;
import cl.orioneta.auth.app.dto.OAuth2ProviderResponse;
import cl.orioneta.auth.app.dto.RefreshTokenRequest;
import cl.orioneta.auth.app.dto.RegisterRequest;
import cl.orioneta.auth.app.dto.TokenValidationResponse;
import cl.orioneta.auth.app.dto.ValidateTokenRequest;
import cl.orioneta.auth.app.service.AuthService;
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
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
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
}
