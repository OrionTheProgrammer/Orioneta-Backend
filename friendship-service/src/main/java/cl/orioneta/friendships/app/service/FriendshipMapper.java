package cl.orioneta.friendships.app.service;

import cl.orioneta.friendships.app.dto.FriendRequestResponse;
import cl.orioneta.friendships.app.dto.FriendshipResponse;
import cl.orioneta.friendships.domain.model.FriendRequest;
import cl.orioneta.friendships.domain.model.Friendship;
import org.springframework.stereotype.Component;

/**
 * Convierte modelos de dominio a respuestas del API.
 */
@Component
public class FriendshipMapper {

    public FriendRequestResponse toRequestResponse(FriendRequest request) {
        return new FriendRequestResponse(
                request.getId(),
                request.getSenderUserId(),
                request.getReceiverUserId(),
                request.getStatus(),
                request.getCreatedAt(),
                request.getRespondedAt()
        );
    }

    public FriendshipResponse toFriendshipResponse(Friendship friendship) {
        return new FriendshipResponse(
                friendship.getId(),
                friendship.getUserId(),
                friendship.getFriendId(),
                friendship.getConversationId(),
                friendship.getStatus(),
                friendship.getCreatedAt(),
                friendship.getUpdatedAt()
        );
    }
}
