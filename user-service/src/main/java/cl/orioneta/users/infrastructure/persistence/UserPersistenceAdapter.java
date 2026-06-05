package cl.orioneta.users.infrastructure.persistence;

import cl.orioneta.users.app.repository.UserRepository;
import cl.orioneta.users.domain.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * Puente entre la aplicacion y JPA.
 *
 * <p>La app habla con {@link UserRepository}; este adaptador traduce ese
 * contrato a {@link JpaUserRepository} y convierte entre dominio y entidad.</p>
 */
@Repository
public class UserPersistenceAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public UserPersistenceAdapter(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        UserEntity savedEntity = jpaUserRepository.save(toEntity(user));
        return toDomain(savedEntity);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findById(UUID userID) {
        return jpaUserRepository.findById(userID)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return jpaUserRepository.findByUserNameIgnoreCase(userName)
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
    public boolean existsByUserName(String userName) {
        return jpaUserRepository.existsByUserNameIgnoreCase(userName);
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
    public void deleteById(UUID userID) {
        jpaUserRepository.deleteById(userID);
    }

    private UserEntity toEntity(User user) {
        return new UserEntity(
                user.getUserID(),
                user.getUserName(),
                user.getDisplayName(),
                user.getBio(),
                user.getEmail(),
                user.getFriendCode(),
                user.getStatus(),
                user.getVisibility(),
                user.getProfilePhoto(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private User toDomain(UserEntity entity) {
        return User.rehidratado(
                entity.getUserID().toString(),
                entity.getUserName(),
                entity.getDisplayName(),
                entity.getBio(),
                entity.getEmail(),
                entity.getFriendCode(),
                entity.getStatus().name(),
                entity.getVisibility().name(),
                entity.getProfilePhoto(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
