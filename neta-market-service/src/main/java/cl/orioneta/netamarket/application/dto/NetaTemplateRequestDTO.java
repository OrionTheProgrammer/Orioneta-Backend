package cl.orioneta.netamarket.application.dto;

import cl.orioneta.netamarket.domain.model.NetaTemplateType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NetaTemplateRequestDTO(
        @NotNull(message = "El autor es obligatorio")
        UUID authorUserId,
        @NotBlank(message = "El nombre es obligatorio")
        String name,
        String description,
        @NotNull(message = "El tipo es obligatorio")
        NetaTemplateType type,
        String previewImageUrl,
        @NotBlank(message = "La URL del archivo es obligatoria")
        String fileUrl,
        String version
) {
}
