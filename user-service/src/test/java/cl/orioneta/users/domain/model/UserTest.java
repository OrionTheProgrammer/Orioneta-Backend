package cl.orioneta.users.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

class UserTest {

    private final Faker faker = new Faker();

    @Test
    void createsUserWithDefaultsAndNormalizedEmail() {
        String email = faker.internet().emailAddress();

        User user = new User(
                safeUsername(),
                safeDisplayName(),
                faker.lorem().sentence(8),
                email.toUpperCase(),
                null,
                null,
                null
        );

        assertThat(user.getUserID()).isNotNull();
        assertThat(user.getEmail()).isEqualTo(email.toLowerCase());
        assertThat(user.getFriendCode()).hasSize(12);
        assertThat(user.getStatus()).isEqualTo(Status.OFFLINE);
        assertThat(user.getVisibility()).isEqualTo(VisibilityStatus.PUBLIC);
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    void rejectsInvalidEmail() {
        assertThatThrownBy(() -> new User(safeUsername(), safeDisplayName(), "", "email-malo", null, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email");
    }

    @Test
    void rehydratesExistingUserKeepingDates() {
        LocalDateTime createdAt = LocalDateTime.now().minusDays(2);
        LocalDateTime updatedAt = LocalDateTime.now().minusDays(1);

        User user = User.rehidratado(
                "7a4314b7-7c7d-48ce-9fe4-89bdbde76e2e",
                "orion",
                "Orion",
                "",
                "orion@orioneta.cl",
                "ABCDEF123456",
                "ONLINE",
                "PRIVATE",
                "",
                createdAt,
                updatedAt
        );

        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
        assertThat(user.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(user.getStatus()).isEqualTo(Status.ONLINE);
        assertThat(user.getVisibility()).isEqualTo(VisibilityStatus.PRIVATE);
    }

    private String safeUsername() {
        return ("user" + faker.number().digits(8)).substring(0, 12);
    }

    private String safeDisplayName() {
        String displayName = faker.name().firstName() + " " + faker.name().lastName();
        return displayName.length() > 60 ? displayName.substring(0, 60) : displayName;
    }
}
