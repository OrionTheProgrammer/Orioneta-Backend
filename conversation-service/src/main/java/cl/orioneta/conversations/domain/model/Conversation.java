package cl.orioneta.conversations.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Conversation {

    // Atributos
    // Nunca cambian
    private final UUID id;
    private final ConversationType type; // ENUM Tipo GRUPO o DIRECTO, para 1 persona o para un grupo de 1 a mas personas
    private final UUID createdBy; // userId del creador
    private final Instant createdAt; // fecha y hora de creacion

    // Si pueden cambiar
    private String title; // titulo del grupo o de la persona
    private String avatarUrl; // url de la foto del grupo o usuario
    private List<Participant> participants; // Participantes dentro de la conversacion
    private Instant updatedAt; // fecha y hora de ultima actualizacion

    // Constructor Privado - nadie lo llama
    private Conversation(UUID id, ConversationType type, UUID createdBy, Instant createdAt, String title, String avatarUrl, List<Participant> participants, Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "El id es obligatorio");
        this.type = Objects.requireNonNull(type, "El tipo es obligatorio");
        this.createdBy = Objects.requireNonNull(createdBy, "El creador es obligatorio");
        this.title = Objects.requireNonNull(title, "El Título es obligatorio");
        this.avatarUrl = avatarUrl;
        this.participants = participants != null ? participants : new ArrayList<>();
        this.createdAt = Objects.requireNonNull(createdAt, "La fecha de creación es obligatorio");
        this.updatedAt = Objects.requireNonNull(updatedAt, "La fecha de actualización es obligatorio");
    }



    // Factory methods
    public static Conversation create(String title, ConversationType type, UUID createdBy) {
        Instant now = Instant.now();
        return new Conversation(UUID.randomUUID(), type, createdBy, now, title, null, new ArrayList<>(), now);
    }


    public static Conversation rehydrate(UUID id, ConversationType type, UUID createdBy, String title, String avatarUrl, List<Participant> participants, Instant createdAt, Instant updatedAt) {
        return new Conversation(id, type, createdBy, createdAt, title, avatarUrl, participants, updatedAt);
    }

    // Metodos //

    // Actualizar titulo del usuario o grupo
    public void updateTitle(String title) {
        this.title = requireText(title, "El título no puede estar vacío");
        touch();
    }

    // Actualizar la imagen del usuario o grupo
    public void updateAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        touch();
    }


    // Añadir Participante
    public void addParticipant(Participant participant) {
        Objects.requireNonNull(participant, "El participante es obligatorio");
        this.participants.add(participant);
        touch();
    }

    // Para poder remover a un participante mediante el userId
    public void removeParticipant(UUID userId) {
        this.participants.removeIf(p -> p.getUserId().equals(userId));
        touch();
    }
    // Getters
    public UUID getId() {
        return id;
    }

    public ConversationType getType() {
        return type;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // Metodos privados de soporte
    private void touch() {
        this.updatedAt = Instant.now();
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }


}
