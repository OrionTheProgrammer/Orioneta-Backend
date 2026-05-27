package cl.orioneta.users.domain.model;

import java.security.SecureRandom;

/**
 * Generates public hexadecimal friend codes for user discovery.
 *
 * <p>The generated value is a candidate. The database or repository adapter must
 * still verify uniqueness before saving a user, because only persistence can
 * know every code that already exists.
 */
public final class FriendCodeGenerator {

    private static final char[] HEX = "0123456789ABCDEF".toCharArray();
    private static final int DEFAULT_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    private FriendCodeGenerator() {
    }

    /**
     * Creates an uppercase hexadecimal code such as {@code A91F23C7}.
     *
     * @return generated friend code candidate
     */
    public static String generate() {
        char[] value = new char[DEFAULT_LENGTH];
        for (int index = 0; index < value.length; index++) {
            value[index] = HEX[RANDOM.nextInt(HEX.length)];
        }
        return new String(value);
    }
}
