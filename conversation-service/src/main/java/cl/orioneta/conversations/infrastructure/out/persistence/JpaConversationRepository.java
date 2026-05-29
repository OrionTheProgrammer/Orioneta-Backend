package cl.orioneta.conversations.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaConversationRepository extends JpaRepository<ConversationEntity, UUID> {

    // Buscar todas las conversaciones donde el usuario es participante
    @Query("SELECT c FROM ConversationEntity c JOIN c.participants p WHERE p.userId = :userId AND p.active = true")
    List<ConversationEntity> findByUserId(@Param("userId") UUID userId);

    // Verificar si existe conversación directa entre dos usuarios
    @Query("""
            SELECT COUNT(c) > 0 FROM ConversationEntity c
            JOIN c.participants p1 ON p1.userId = :userIdA
            JOIN c.participants p2 ON p2.userId = :userIdB
            WHERE c.type = 'DIRECT'
            AND p1.active = true AND p2.active = true
            """)
    boolean existsDirectConversation(@Param("userIdA") UUID userIdA, @Param("userIdB") UUID userIdB);
}
