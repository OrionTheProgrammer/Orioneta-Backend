package cl.orioneta.users.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.orioneta.users.app.dto.CreateUserRequest;
import cl.orioneta.users.app.dto.UpdateUserProfileRequest;
import cl.orioneta.users.app.dto.UpdateUserStatusRequest;
import cl.orioneta.users.app.dto.UserResponse;
import cl.orioneta.users.app.repository.UserRepository;
import cl.orioneta.users.domain.exception.UserAlreadyExistsException;
import cl.orioneta.users.domain.model.Status;
import cl.orioneta.users.domain.model.User;
import java.util.Optional;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserServiceTest {

    private final Faker faker = new Faker();

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository, new UserMapper());
    }

    @Test
    void createsUser() {
        CreateUserRequest request = fakeCreateUserRequest();

        when(userRepository.existsByUserName(request.userName())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userRepository.existsByFriendCode(any())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.createUser(request);

        assertThat(response.userID()).isNotNull();
        assertThat(response.userName()).isEqualTo(request.userName());
        assertThat(response.email()).isEqualTo(request.email().toLowerCase());
        assertThat(response.status()).isEqualTo(Status.OFFLINE);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void rejectsDuplicatedEmailIgnoringCase() {
        CreateUserRequest request = fakeCreateUserRequest();

        when(userRepository.existsByUserName(request.userName())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("email");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updatesProfileAndStatus() {
        User user = fakeUser();
        String displayName = safeDisplayName();
        String bio = faker.lorem().sentence(6);
        String profilePhoto = "https://cdn.orioneta.cl/" + faker.file().fileName();

        when(userRepository.findById(user.getUserID())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse updatedProfile = userService.updateProfile(
                user.getUserID(),
                new UpdateUserProfileRequest(displayName, bio, profilePhoto)
        );
        UserResponse updatedStatus = userService.updateStatus(user.getUserID(), new UpdateUserStatusRequest(Status.BUSY));

        assertThat(updatedProfile.displayName()).isEqualTo(displayName);
        assertThat(updatedProfile.bio()).isEqualTo(bio);
        assertThat(updatedStatus.status()).isEqualTo(Status.BUSY);
    }

    private CreateUserRequest fakeCreateUserRequest() {
        return new CreateUserRequest(
                safeUsername(),
                safeDisplayName(),
                faker.lorem().sentence(8),
                faker.internet().emailAddress().toLowerCase(),
                null
        );
    }

    private User fakeUser() {
        CreateUserRequest request = fakeCreateUserRequest();

        return new User(
                request.userName(),
                request.displayName(),
                request.bio(),
                request.email(),
                null,
                null,
                request.profilePhoto()
        );
    }

    private String safeUsername() {
        return ("user" + faker.number().digits(8)).substring(0, 12);
    }

    private String safeDisplayName() {
        String displayName = faker.name().firstName() + " " + faker.name().lastName();
        return displayName.length() > 60 ? displayName.substring(0, 60) : displayName;
    }
}
