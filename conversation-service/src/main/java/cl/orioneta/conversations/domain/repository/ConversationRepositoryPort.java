package cl.orioneta.conversations.domain.repository;

import cl.orioneta.conversations.domain.model.Conversation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepositoryPort {

    // Guardar conversacion nueva o actualizada
    Conversation save(Conversation conversation);

    // Buscar conversacion por id
    Optional<Conversation> findById(UUID conversationId);

    // Buscar todas las conversaciones de un usuario
    List<Conversation> findByUserId(UUID userId);

    // Verificar si ya existe una conversacion directa entre dos usuario
    boolean existsDirectConversation(UUID userIdA, UUID userIdB);

    // Eliminar conversacion por id
    void deleteById(UUID id);


}
