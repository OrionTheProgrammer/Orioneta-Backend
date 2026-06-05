package cl.orioneta.gateway.filter;

/**
 * Punto reservado para validaciones adicionales de JWT en el gateway.
 *
 * <p>La validacion real se activara desde {@code SecurityConfig} cuando el
 * flujo auth-service + frontend este cerrado. Mantener esta clase documentada
 * evita meter logica manual de tokens antes de necesitarla.</p>
 */
public class JwtAuthenticationFilter {
}
