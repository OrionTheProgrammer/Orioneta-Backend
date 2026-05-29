package cl.orioneta.conversations.application.command;

import java.util.List;
import java.util.UUID;

public class CreateGroupConversationCommand {

    private final UUID creatorId; // quien crea el grupo
    private final String title; // nombre del grupo
    private final List<UUID> memberIds; // Miembros iniciales del grupo

    public CreateGroupConversationCommand(UUID creatorId, String title, List<UUID> memberIds) {
        this.creatorId = creatorId;
        this.title = title;
        this.memberIds = memberIds;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public String getTitle() {
        return title;
    }

    public List<UUID> getMemberIds() {
        return memberIds;
    }
}
