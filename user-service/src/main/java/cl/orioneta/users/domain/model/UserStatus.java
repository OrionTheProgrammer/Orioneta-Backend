package cl.orioneta.users.domain.model;

/**
 * Represents the presence state shown to friends and active conversations.
 *
 * <p>The enum only expresses the domain vocabulary. Synchronizing status across
 * devices or WebSocket sessions belongs to application services and
 * {@code realtime-service}.
 */
public enum UserStatus {
    ONLINE,
    OFFLINE,
    AWAY
}
