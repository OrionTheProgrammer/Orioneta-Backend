package cl.orioneta.friendships.infrastructure.web;

import cl.orioneta.friendships.app.dto.BlockUserRequest;
import cl.orioneta.friendships.app.dto.FriendRequestResponse;
import cl.orioneta.friendships.app.dto.FriendshipResponse;
import cl.orioneta.friendships.app.dto.RespondFriendRequest;
import cl.orioneta.friendships.app.dto.SendFriendRequest;
import cl.orioneta.friendships.app.service.FriendshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST para solicitudes, amistades y bloqueos.
 */
@RestController
@RequestMapping("/api/friendships")
@Tag(name = "Amistades", description = "Solicitudes de amistad, lista de amigos y bloqueos.")
public class FriendshipController {

    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Enviar solicitud", description = "Envia una solicitud por id, email o friend code.")
    public FriendRequestResponse sendFriendRequest(@Valid @RequestBody SendFriendRequest request) {
        return friendshipService.sendFriendRequest(request);
    }

    @PatchMapping("/requests/{requestId}/accept")
    @Operation(summary = "Aceptar solicitud", description = "Acepta una solicitud pendiente y crea la amistad.")
    public FriendshipResponse acceptRequest(@PathVariable UUID requestId, @Valid @RequestBody RespondFriendRequest request) {
        return friendshipService.acceptRequest(requestId, request);
    }

    @PatchMapping("/requests/{requestId}/reject")
    @Operation(summary = "Rechazar solicitud", description = "Rechaza una solicitud pendiente.")
    public FriendRequestResponse rejectRequest(@PathVariable UUID requestId, @Valid @RequestBody RespondFriendRequest request) {
        return friendshipService.rejectRequest(requestId, request);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    @Operation(summary = "Cancelar solicitud", description = "Cancela una solicitud pendiente enviada por el usuario.")
    public FriendRequestResponse cancelRequest(@PathVariable UUID requestId, @Valid @RequestBody RespondFriendRequest request) {
        return friendshipService.cancelRequest(requestId, request);
    }

    @GetMapping("/users/{userId}/requests/received")
    @Operation(summary = "Solicitudes recibidas", description = "Lista solicitudes recibidas por un usuario.")
    public List<FriendRequestResponse> listReceivedRequests(@PathVariable UUID userId) {
        return friendshipService.listReceivedRequests(userId);
    }

    @GetMapping("/users/{userId}/requests/sent")
    @Operation(summary = "Solicitudes enviadas", description = "Lista solicitudes enviadas por un usuario.")
    public List<FriendRequestResponse> listSentRequests(@PathVariable UUID userId) {
        return friendshipService.listSentRequests(userId);
    }

    @GetMapping("/users/{userId}/friends")
    @Operation(summary = "Listar amigos", description = "Lista amistades activas de un usuario.")
    public List<FriendshipResponse> listFriends(@PathVariable UUID userId) {
        return friendshipService.listFriends(userId);
    }

    @PatchMapping("/users/{userId}/friends/{friendId}/remove")
    @Operation(summary = "Eliminar amigo", description = "Marca una amistad como removida.")
    public FriendshipResponse removeFriend(@PathVariable UUID userId, @PathVariable UUID friendId) {
        return friendshipService.removeFriend(userId, friendId);
    }

    @PatchMapping("/block")
    @Operation(summary = "Bloquear usuario", description = "Bloquea la relacion entre dos usuarios.")
    public FriendshipResponse blockUser(@Valid @RequestBody BlockUserRequest request) {
        return friendshipService.blockUser(request);
    }
}
