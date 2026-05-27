package cl.orioneta.users.domain.exception;

/**
 * Excepcion base para reglas de dominio incumplidas en usuarios.
 *
 * <p>Se lanza cuando el modelo recibe datos invalidos, como usernames vacios o
 * codigos de amistad mal formados. Los controladores deberian traducirla a una
 * respuesta entendible para el cliente, mientras el dominio sigue independiente
 * de Spring MVC.
 */
public class UserGlobalException extends RuntimeException {

    /**
     * Crea una excepcion de dominio con un mensaje de negocio legible.
     *
     * @param message explicacion de la regla incumplida
     */
    public UserGlobalException(String message) {
        super(message);
    }
}
