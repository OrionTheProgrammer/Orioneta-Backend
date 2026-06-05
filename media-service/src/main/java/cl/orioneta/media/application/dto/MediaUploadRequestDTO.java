package cl.orioneta.media.application.dto;

import cl.orioneta.media.domain.model.MediaPurpose;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Datos para registrar un archivo ya subido o referenciado.
 */
public record MediaUploadRequestDTO(
        @NotNull(message = "El owner es obligatorio")
        UUID ownerUserId,

        @NotBlank(message = "El nombre del archivo es obligatorio")
        String fileName,

        @NotBlank(message = "El content type es obligatorio")
        String contentType,

        @Min(value = 0, message = "El tamano no puede ser negativo")
        long size,

        @NotBlank(message = "La URL es obligatoria")
        String url,

        @NotNull(message = "El proposito es obligatorio")
        MediaPurpose purpose
) {
}
