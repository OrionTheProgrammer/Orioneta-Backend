package cl.orioneta.users.application.dto;

import cl.orioneta.users.domain.model.AccountVisibility;
import cl.orioneta.users.domain.model.UserStatus;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Respuesta entregada por endpoints relacionados con usuarios.
 *
 * <p>Expone la identidad publica que necesitan BFF, amistad y conversaciones.
 * Los detalles sensibles de autenticacion no deben agregarse a este DTO.
 *
 * @param id id interno del usuario
 * @param username username unico
 * @param displayName nombre visible
 * @param email correo asociado al perfil
 * @param friendCode codigo hexadecimal publico para agregar amigos
 * @param avatarUrl URL opcional de avatar
 * @param bannerUrl URL opcional de banner
 * @param bio biografia opcional
 * @param status estado de presencia actual
 * @param accountVisibility regla de visibilidad
 * @param createdAt fecha de creacion
 * @param updatedAt fecha de ultima actualizacion
 */
public record UserResponseDTO(
        UUID id,
        String username,
        String displayName,
        String email,
        String friendCode,
        String avatarUrl,
        String bannerUrl,
        String bio,
        UserStatus status,
        AccountVisibility accountVisibility,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
