package cl.orioneta.conversations.domain.model;

import java.sql.Timestamp;
import java.util.UUID;

public class Participant {

    private UUID id;
    private Long conversation_id;
    private Long user_id;
    private String role; // ADMIN o MIEMBRO | ADMIN or MEMBER
    private String joined_do; // UNIDO por alguien
    private Timestamp last_read_at; // Ultima vez escrito
    private Boolean is_muted; // Mutear
    private Boolean is_active; // Activo


}
