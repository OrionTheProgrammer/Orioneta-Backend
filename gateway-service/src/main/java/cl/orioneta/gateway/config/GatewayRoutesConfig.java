package cl.orioneta.gateway.config;

import org.springframework.context.annotation.Configuration;

/**
 * Marca el lugar donde viven las rutas del gateway.
 *
 * <p>Las rutas se declaran en {@code application.yml} porque asi es mas facil
 * cambiar URLs por ambiente sin recompilar el servicio.</p>
 */
@Configuration
public class GatewayRoutesConfig {
}
