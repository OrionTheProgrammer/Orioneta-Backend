package cl.orioneta.friendships.app.event;

import cl.orioneta.friendships.domain.event.FriendRequestAcceptedEvent;
import cl.orioneta.friendships.domain.event.FriendRequestSentEvent;

/**
 * Puerto para publicar eventos de amistad.
 */
public interface FriendshipEventPublisher {

    void publishFriendRequestSent(FriendRequestSentEvent event);

    void publishFriendRequestAccepted(FriendRequestAcceptedEvent event);
}
