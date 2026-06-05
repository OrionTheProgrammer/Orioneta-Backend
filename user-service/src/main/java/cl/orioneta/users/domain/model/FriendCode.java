package cl.orioneta.users.domain.model;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utilidad de dominio para generar y validar codigos publicos de amistad.
 */
public class FriendCode {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
    private static final Pattern HEX_PATTERN = Pattern.compile("^[0-9a-fA-F]+$");
    private static final int DEFAULT_LENGTH = 12;

    protected static String friendCodeGen(int length){
        if (length <= 0) {
            throw new IllegalArgumentException("El largo del codigo de amigo debe ser mayor a cero");
        }

        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++){
            int randomIndex = RANDOM.nextInt(HEX_CHARS.length);
            code.append(HEX_CHARS[randomIndex]);
        }

        return code.toString();
    }

    /**
     * Valida un codigo usando el largo por defecto de Orioneta.
     */
    public static String codeValidator(String code){
        return codeValidator(code, DEFAULT_LENGTH);
    }

    /**
     * Valida un codigo hexadecimal con un largo esperado.
     */
    protected static String codeValidator(String code, int expectedLength){
        if (code == null || code.isBlank()){
            throw new IllegalArgumentException("El codigo de amigo no puede ser null o estar vacio");
        }

        String normalizedCode = code.trim().toUpperCase(Locale.ROOT);

        if (normalizedCode.length() != expectedLength) {
            throw new IllegalArgumentException("El codigo de amigo debe tener " + expectedLength + " caracteres");
        }else if (!HEX_PATTERN.matcher(normalizedCode).matches()){
            throw new IllegalArgumentException("El codigo de amigo no es valido");
        }

        return normalizedCode;
    }

}
