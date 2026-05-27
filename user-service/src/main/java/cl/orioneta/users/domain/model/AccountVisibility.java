package cl.orioneta.users.domain.model;

/**
 * Define que tan visible es un perfil para las funciones sociales.
 *
 * <p>El enum pertenece a {@code user-service} porque la visibilidad forma parte
 * de la identidad publica. Servicios como {@code friendship-service} deben
 * consultar este dato en vez de duplicar la regla.
 */
public enum AccountVisibility {
    PUBLIC,
    FRIENDS_ONLY,
    PRIVATE
}
