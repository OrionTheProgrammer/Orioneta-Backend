package cl.orioneta.users.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cl.orioneta.users.app.dto.CreateUserRequest;
import cl.orioneta.users.app.dto.UpdateUserProfileRequest;
import cl.orioneta.users.app.dto.UpdateUserStatusRequest;
import cl.orioneta.users.app.dto.UserResponse;
import cl.orioneta.users.app.repository.UserRepository;
import cl.orioneta.users.domain.exception.UserAlreadyExistsException;
import cl.orioneta.users.domain.model.Status;
import cl.orioneta.users.domain.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private InMemoryUserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        userService = new UserService(userRepository, new UserMapper());
    }

    @Test
    void createsUser() {
        UserResponse response = userService.createUser(new CreateUserRequest(
                "orion",
                "Orion",
                "Creador de Orioneta",
                "orion@orioneta.cl",
                null
        ));

        assertThat(response.userID()).isNotNull();
        assertThat(response.userName()).isEqualTo("orion");
        assertThat(response.status()).isEqualTo(Status.OFFLINE);
        assertThat(userRepository.findAll()).hasSize(1);
    }

    @Test
    void rejectsDuplicatedEmailIgnoringCase() {
        userService.createUser(new CreateUserRequest("orion", "Orion", "", "orion@orioneta.cl", null));

        assertThatThrownBy(() -> userService.createUser(new CreateUserRequest(
                "orion2",
                "Orion Dos",
                "",
                "ORION@ORIONETA.CL",
                null
        )))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("email");
    }

    @Test
    void updatesProfileAndStatus() {
        UserResponse created = userService.createUser(new CreateUserRequest("orion", "Orion", "", "orion@orioneta.cl", null));

        UserResponse updatedProfile = userService.updateProfile(
                created.userID(),
                new UpdateUserProfileRequest("Orioneta", "Nueva bio", "https://cdn.orioneta.cl/avatar.png")
        );
        UserResponse updatedStatus = userService.updateStatus(created.userID(), new UpdateUserStatusRequest(Status.BUSY));

        assertThat(updatedProfile.displayName()).isEqualTo("Orioneta");
        assertThat(updatedProfile.bio()).isEqualTo("Nueva bio");
        assertThat(updatedStatus.status()).isEqualTo(Status.BUSY);
    }

    private static class InMemoryUserRepository implements UserRepository {

        private final List<User> users = new ArrayList<>();

        @Override
        public User save(User user) {
            deleteById(user.getUserID());
            users.add(user);
            return user;
        }

        @Override
        public List<User> findAll() {
            return List.copyOf(users);
        }

        @Override
        public Optional<User> findById(UUID userID) {
            return users.stream()
                    .filter(user -> user.getUserID().equals(userID))
                    .findFirst();
        }

        @Override
        public Optional<User> findByUserName(String userName) {
            return users.stream()
                    .filter(user -> user.getUserName().equalsIgnoreCase(userName))
                    .findFirst();
        }

        @Override
        public Optional<User> findByEmail(String email) {
            return users.stream()
                    .filter(user -> user.getEmail().equals(normalizeEmail(email)))
                    .findFirst();
        }

        @Override
        public Optional<User> findByFriendCode(String friendCode) {
            return users.stream()
                    .filter(user -> user.getFriendCode().equals(friendCode))
                    .findFirst();
        }

        @Override
        public boolean existsByUserName(String userName) {
            return findByUserName(userName).isPresent();
        }

        @Override
        public boolean existsByEmail(String email) {
            return findByEmail(email).isPresent();
        }

        @Override
        public boolean existsByFriendCode(String friendCode) {
            return findByFriendCode(friendCode).isPresent();
        }

        @Override
        public void deleteById(UUID userID) {
            users.removeIf(user -> user.getUserID().equals(userID));
        }

        private String normalizeEmail(String email) {
            return email.trim().toLowerCase(Locale.ROOT);
        }
    }
}
