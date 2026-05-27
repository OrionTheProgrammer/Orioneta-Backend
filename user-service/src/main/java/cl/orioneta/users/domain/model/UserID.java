package cl.orioneta.users.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Objeto de valor que identifica a un usuario dentro de Orioneta.
 *
 * <p>El dominio crea el id antes de llegar a persistencia para que los casos de
 * uso puedan publicar eventos, correlacionar logs y construir respuestas sin
 * esperar un valor generado por la base de datos. Un adaptador tambien puede
 * reconstruir este objeto con un UUID existente mediante {@link #UserID(UUID)}.
 */
public class UserID {

    private final UUID value;

    /**
     * Crea un id aleatorio para un usuario nuevo.
     */
    public UserID() {
        this.value = UUID.randomUUID();
    }

    /**
     * Reconstruye un id que ya existe en persistencia.
     *
     * @param value valor UUID existente
     */
    public UserID(UUID value) {
        this.value = Objects.requireNonNull(value, "El valor del UserID es obligatorio.");
    }

    /**
     * Convierte un texto UUID en un objeto de valor {@code UserID}.
     *
     * @param value UUID en formato texto
     * @return objeto de valor del id de usuario
     */
    public static UserID fromString(String value) {
        return new UserID(UUID.fromString(value));
    }

    /**
     * Entrega el UUID usado por persistencia y DTOs externos.
     *
     * @return valor UUID interno
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
