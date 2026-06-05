package cl.orioneta.friendships.app.client;

import cl.orioneta.friendships.app.dto.UserSummary;
import java.util.UUID;

/**
 * Puerto para consultar usuarios en user-service.
 *
 * <p>La aplicacion necesita saber si un usuario existe, pero no debe conocer
 * Feign, HTTP ni URLs. Eso queda en infraestructura.</p>
 */
public interface UserDirectory {

    UserSummary findById(UUID userId);

    UserSummary findByEmail(String email);

    UserSummary findByFriendCode(String friendCode);
}
