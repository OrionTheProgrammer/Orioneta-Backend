package cl.orioneta.users.domain.model;

/**
 * Representa el estado de presencia visible para amigos y conversaciones.
 *
 * <p>El enum solo define vocabulario de dominio. Sincronizar el estado entre
 * dispositivos o sesiones WebSocket corresponde a casos de uso y al
 * {@code realtime-service}.
 */
public enum UserStatus {
    ONLINE,
    OFFLINE,
    AWAY,
    BUSY,
    INVISIBLE
}
