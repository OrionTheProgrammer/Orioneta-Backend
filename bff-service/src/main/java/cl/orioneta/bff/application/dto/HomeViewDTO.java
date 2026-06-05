package cl.orioneta.bff.application.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Vista inicial que consume el frontend al abrir Orioneta.
 */
public record HomeViewDTO(
        UUID userId,
        Map<String, Object> user,
        List<Map<String, Object>> conversations,
        List<Map<String, Object>> notifications,
        Map<String, Object> customization
) {
}
