package cl.orioneta.friendships.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * Datos para enviar una solicitud de amistad.
 *
 * <p>El destino puede resolverse por {@code receiverUserId}, por email o por
 * friend code. Basta con uno de esos datos.</p>
 */
public record SendFriendRequest(
        @NotNull(message = "El usuario emisor es obligatorio")
        UUID senderUserId,

        UUID receiverUserId,

        @Email(message = "El email no tiene un formato valido")
        @Size(max = 120, message = "El email no puede superar los 120 caracteres")
        String receiverEmail,

        @Size(min = 12, max = 12, message = "El friend code debe tener 12 caracteres")
        String receiverFriendCode
) {
}
