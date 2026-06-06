package cl.orioneta.messages.application.conversation;

import java.util.UUID;

/**
 * Puerto para consultar conversation-service sin acoplar los casos de uso a
 * Feign ni a detalles HTTP.
 */
public interface ConversationLookupPort {

    ConversationSummary findById(UUID conversationId);
}
