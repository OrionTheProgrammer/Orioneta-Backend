package cl.orioneta.auth.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank String email,
        @NotBlank String token,
        @NotBlank @Size(min = 8) String newPassword
) {}