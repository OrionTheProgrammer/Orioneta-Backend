package cl.orioneta.auth.app.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifyResetCodeRequest(@NotBlank String email, @NotBlank String code) {}