package cl.orioneta.friendships.app.client;

import java.util.UUID;

/**
 * Puerto de aplicacion para crear conversaciones desde el flujo de amistad.
 *
 * <p>El servicio de amistades no conoce HTTP ni detalles de conversation-service.
 * Solo expresa la necesidad de abrir un chat privado entre dos usuarios cuando
 * una solicitud es aceptada.</p>
 */
public interface ConversationDirectory {

    /**
     * Crea un chat privado entre dos usuarios.
     *
     * @param firstUserId primer participante de la conversacion
     * @param secondUserId segundo participante de la conversacion
     * @return identificador de la conversacion creada
     */
    UUID createPrivateConversation(UUID firstUserId, UUID secondUserId);
}
