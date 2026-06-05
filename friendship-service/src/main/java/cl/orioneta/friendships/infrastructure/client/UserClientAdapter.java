package cl.orioneta.friendships.infrastructure.client;

import cl.orioneta.friendships.app.client.UserDirectory;
import cl.orioneta.friendships.app.dto.UserSummary;
import cl.orioneta.friendships.domain.exception.UserLookupException;
import feign.FeignException;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Adaptador que convierte respuestas HTTP de user-service al puerto de app.
 */
@Component
public class UserClientAdapter implements UserDirectory {

    private final UserClient userClient;

    public UserClientAdapter(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public UserSummary findById(UUID userId) {
        return toSummary(callUserService(() -> userClient.findById(userId)));
    }

    @Override
    public UserSummary findByEmail(String email) {
        return toSummary(callUserService(() -> userClient.findByEmail(email)));
    }

    @Override
    public UserSummary findByFriendCode(String friendCode) {
        return toSummary(callUserService(() -> userClient.findByFriendCode(friendCode)));
    }

    private UserClientResponse callUserService(UserClientCall call) {
        try {
            return call.execute();
        } catch (FeignException.NotFound exception) {
            throw new UserLookupException("Usuario no encontrado");
        } catch (FeignException exception) {
            throw new UserLookupException("No se pudo consultar user-service");
        }
    }

    private UserSummary toSummary(UserClientResponse response) {
        return new UserSummary(
                response.userID(),
                response.userName(),
                response.displayName(),
                response.email(),
                response.friendCode()
        );
    }

    @FunctionalInterface
    private interface UserClientCall {
        UserClientResponse execute();
    }
}
