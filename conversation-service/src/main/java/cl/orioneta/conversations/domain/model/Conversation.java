package cl.orioneta.conversations.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Conversacion privada o grupal de Orioneta.
 */
public class Conversation {

    private final UUID id;
    private ConversationType type;
    private String name;
    private String description;
    private UUID ownerId;
    private String avatarUrl;
    private String backgroundUrl;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private final List<Participant> participants;

    private Conversation(
            UUID id,
            ConversationType type,
            String name,
            String description,
            UUID ownerId,
            String avatarUrl,
            String backgroundUrl,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt,
            List<Participant> participants
    ) {
        this.id = Objects.requireNonNull(id, "El id de la conversacion es obligatorio");
        this.type = Objects.requireNonNull(type, "El tipo de conversacion es obligatorio");
        this.name = normalizeOptional(name);
        this.description = normalizeOptional(description);
        this.ownerId = ownerId;
        this.avatarUrl = normalizeOptional(avatarUrl);
        this.backgroundUrl = normalizeOptional(backgroundUrl);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.updatedAt = updatedAt == null ? this.createdAt : updatedAt;
        this.deletedAt = deletedAt;
        this.participants = new ArrayList<>(participants == null ? List.of() : participants);
        validate();
    }

    public static Conversation createPrivate(UUID firstUserId, UUID secondUserId) {
        if (Objects.equals(firstUserId, secondUserId)) {
            throw new IllegalArgumentException("Un chat privado necesita dos usuarios distintos");
        }

        UUID conversationId = UUID.randomUUID();

        return new Conversation(
                conversationId,
                ConversationType.PRIVATE_CHAT,
                "",
                "",
                null,
                "",
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                List.of(
                        Participant.create(conversationId, firstUserId, ParticipantRole.MEMBER),
                        Participant.create(conversationId, secondUserId, ParticipantRole.MEMBER)
                )
        );
    }

    public static Conversation createGroup(UUID ownerId, String name, String description, List<UUID> memberIds) {
        UUID conversationId = UUID.randomUUID();
        List<Participant> participants = new ArrayList<>();
        participants.add(Participant.create(conversationId, ownerId, ParticipantRole.OWNER));

        for (UUID memberId : memberIds == null ? List.<UUID>of() : memberIds) {
            if (!ownerId.equals(memberId)) {
                participants.add(Participant.create(conversationId, memberId, ParticipantRole.MEMBER));
            }
        }

        return new Conversation(
                conversationId,
                ConversationType.GROUP_CHAT,
                requireText(name, "El nombre del grupo es obligatorio"),
                description,
                ownerId,
                "",
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                participants
        );
    }

    public static Conversation rehydrate(
            UUID id,
            ConversationType type,
            String name,
            String description,
            UUID ownerId,
            String avatarUrl,
            String backgroundUrl,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt,
            List<Participant> participants
    ) {
        return new Conversation(id, type, name, description, ownerId, avatarUrl, backgroundUrl, createdAt, updatedAt, deletedAt, participants);
    }

    public void addParticipant(UUID userId, ParticipantRole role) {
        if (type != ConversationType.GROUP_CHAT) {
            throw new IllegalStateException("Solo los grupos permiten agregar participantes");
        }

        boolean alreadyExists = participants.stream().anyMatch(participant -> participant.getUserId().equals(userId));

        if (!alreadyExists) {
            participants.add(Participant.create(id, userId, role));
            touch();
        }
    }

    public boolean hasParticipant(UUID userId) {
        return participants.stream().anyMatch(participant -> participant.getUserId().equals(userId));
    }

    public UUID getId() {
        return id;
    }

    public ConversationType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public List<Participant> getParticipants() {
        return List.copyOf(participants);
    }

    private void validate() {
        if (type == ConversationType.PRIVATE_CHAT && participants.size() != 2) {
            throw new IllegalArgumentException("Un chat privado necesita exactamente dos participantes");
        }

        if (type == ConversationType.GROUP_CHAT && ownerId == null) {
            throw new IllegalArgumentException("Un grupo necesita owner");
        }
    }

    private void touch() {
        updatedAt = LocalDateTime.now();
    }

    private static String normalizeOptional(String value) {
        return value == null ? "" : value.trim();
    }

    private static String requireText(String value, String message) {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }
}
