package cl.orioneta.users.domain.model;

import java.security.SecureRandom;

/**
 * Genera codigos hexadecimales publicos para encontrar usuarios.
 *
 * <p>El valor generado es un candidato. La base de datos o el adaptador de
 * repositorio deben verificar unicidad antes de guardar, porque solo
 * persistencia conoce todos los codigos existentes.
 */
public final class FriendCodeGenerator {

    private static final char[] HEX = "0123456789ABCDEF".toCharArray();
    private static final int DEFAULT_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    private FriendCodeGenerator() {
    }

    /**
     * Crea un codigo hexadecimal en mayusculas, por ejemplo {@code A91F23C7}.
     *
     * @return candidato de codigo de amistad generado
     */
    public static String generate() {
        char[] value = new char[DEFAULT_LENGTH];
        for (int index = 0; index < value.length; index++) {
            value[index] = HEX[RANDOM.nextInt(HEX.length)];
        }
        return new String(value);
    }
}
