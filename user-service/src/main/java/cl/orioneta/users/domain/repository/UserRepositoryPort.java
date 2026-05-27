package cl.orioneta.users.domain.repository;

import java.util.List;
import java.util.Optional;

import cl.orioneta.users.domain.model.User;
import cl.orioneta.users.domain.model.UserID;

/**
 * Output port used by application use cases to persist and search users.
 *
 * <p>The domain and application layers depend on this interface instead of JPA.
 * Infrastructure adapters will implement it using repositories, HTTP clients or
 * test doubles without forcing business code to know those details.
 */
public interface UserRepositoryPort {

    /**
     * Persists a user aggregate.
     *
     * @param user user to save
     * @return saved user
     */
    User save(User user);

    /**
     * Finds a user by internal id.
     *
     * @param id internal user id
     * @return user when found
     */
    Optional<User> findById(UserID id);

    /**
     * Finds a user by unique username.
     *
     * @param username username to search
     * @return user when found
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by unique email.
     *
     * @param email email to search
     * @return user when found
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by public hexadecimal friend code.
     *
     * @param friendCode public code used by friendship flows
     * @return user when found
     */
    Optional<User> findByFriendCode(String friendCode);

    /**
     * Lists all users available to the current adapter.
     *
     * @return users
     */
    List<User> findAll();

    /**
     * Checks username uniqueness before creating a user.
     *
     * @param username username to check
     * @return true when the username already exists
     */
    boolean existsByUsername(String username);

    /**
     * Checks email uniqueness before creating a user.
     *
     * @param email email to check
     * @return true when the email already exists
     */
    boolean existsByEmail(String email);

    /**
     * Checks friend code uniqueness before saving a generated code.
     *
     * @param friendCode friend code candidate
     * @return true when the code already exists
     */
    boolean existsByFriendCode(String friendCode);

    /**
     * Deletes a user by internal id.
     *
     * @param id user id to delete
     */
    void deleteById(UserID id);
}
