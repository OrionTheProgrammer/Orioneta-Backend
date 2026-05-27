package cl.orioneta.users.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value object that identifies a user inside Orioneta.
 *
 * <p>The domain creates an id before the user reaches persistence so use cases
 * can publish events, correlate logs and compose responses without waiting for a
 * database-generated value. The persistence adapter can also rehydrate this
 * object with an existing UUID by using {@link #UserID(UUID)}.
 */
public class UserID {

    private final UUID value;

    /**
     * Creates a new random user id.
     */
    public UserID() {
        this.value = UUID.randomUUID();
    }

    /**
     * Rehydrates a user id that already exists in persistence.
     *
     * @param value existing UUID value
     */
    public UserID(UUID value) {
        this.value = Objects.requireNonNull(value, "El valor del UserID es obligatorio.");
    }

    /**
     * Parses a UUID string into a user id value object.
     *
     * @param value UUID as text
     * @return user id value object
     */
    public static UserID fromString(String value) {
        return new UserID(UUID.fromString(value));
    }

    /**
     * Returns the raw UUID used by persistence and external DTOs.
     *
     * @return raw UUID value
     */
    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof UserID userID)) {
            return false;
        }
        return value.equals(userID.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
