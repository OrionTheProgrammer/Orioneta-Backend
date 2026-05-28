package cl.orioneta.users.infrastructure.out.persistence;

import cl.orioneta.users.domain.model.User;
import cl.orioneta.users.domain.repository.UserRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * Adaptador de salida que conecta el puerto de dominio con Spring Data JPA.
 *
 * <p>Centraliza el mapeo entre {@link User} y {@link UserEntity}. Si mas
 * adelante cambia la tecnologia de persistencia, los casos de uso no deberian
 * cambiar mientras este puerto conserve el mismo contrato.
 */
@Repository
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaUserRepository;

    /**
     * Crea el adaptador con el repositorio JPA.
     *
     * @param jpaUserRepository repositorio tecnico de Spring Data
     */
    public UserPersistenceAdapter(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        UserEntity saved = jpaUserRepository.save(toEntity(user));
        return toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaUserRepository.findByUsername(username)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findByFriendCode(String friendCode) {
        return jpaUserRepository.findByFriendCode(friendCode)
                .map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaUserRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByFriendCode(String friendCode) {
        return jpaUserRepository.existsByFriendCode(friendCode);
    }

    @Override
    public void deleteById(UUID id) {
        jpaUserRepository.deleteById(id);
    }

    private UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getEmail(),
                user.getFriendCode(),
                user.getAvatarUrl(),
                user.getBannerUrl(),
                user.getBio(),
                user.getStatus(),
                user.getAccountVisibility(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private User toDomain(UserEntity entity) {
        return User.rehydrate(
                entity.getId(),
                entity.getUsername(),
                entity.getDisplayName(),
                entity.getEmail(),
                entity.getFriendCode(),
                entity.getAvatarUrl(),
                entity.getBannerUrl(),
                entity.getBio(),
                entity.getStatus(),
                entity.getAccountVisibility(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
