package cl.orioneta.users.infrastructure.web;

import cl.orioneta.users.app.dto.CreateUserRequest;
import cl.orioneta.users.app.dto.UpdateUserProfileRequest;
import cl.orioneta.users.app.dto.UpdateUserStatusRequest;
import cl.orioneta.users.app.dto.UpdateUserVisibilityRequest;
import cl.orioneta.users.app.dto.UserResponse;
import cl.orioneta.users.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST para perfiles de usuario.
 *
 * <p>El controlador solo recibe HTTP y llama a {@link UserService}. Asi la
 * logica queda en app/domain y no mezclada con anotaciones web.</p>
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "Gestion de perfiles publicos, estados y friend codes.")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear usuario", description = "Crea el perfil publico inicial de un usuario.")
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Lista todos los usuarios registrados.")
    public List<UserResponse> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{userID}")
    @Operation(summary = "Buscar por id", description = "Busca un usuario por su id interno.")
    public UserResponse findUserById(@PathVariable UUID userID) {
        return userService.findUserById(userID);
    }

    @GetMapping("/username/{userName}")
    @Operation(summary = "Buscar por username", description = "Busca un usuario por su username publico.")
    public UserResponse findUserByUserName(@PathVariable String userName) {
        return userService.findUserByUserName(userName);
    }

    @GetMapping("/friend-code/{friendCode}")
    @Operation(summary = "Buscar por friend code", description = "Busca un usuario por su codigo publico de amistad.")
    public UserResponse findUserByFriendCode(@PathVariable String friendCode) {
        return userService.findUserByFriendCode(friendCode);
    }

    @GetMapping("/lookup")
    @Operation(summary = "Buscar por email", description = "Endpoint pensado para friendship-service al agregar amigos por correo.")
    public UserResponse findUserByEmail(@RequestParam String email) {
        return userService.findUserByEmail(email);
    }

    @PatchMapping("/{userID}/profile")
    @Operation(summary = "Actualizar perfil", description = "Edita nombre visible, biografia o foto de perfil.")
    public UserResponse updateProfile(@PathVariable UUID userID, @Valid @RequestBody UpdateUserProfileRequest request) {
        return userService.updateProfile(userID, request);
    }

    @PatchMapping("/{userID}/status")
    @Operation(summary = "Actualizar estado", description = "Cambia el estado de presencia del usuario.")
    public UserResponse updateStatus(@PathVariable UUID userID, @Valid @RequestBody UpdateUserStatusRequest request) {
        return userService.updateStatus(userID, request);
    }

    @PatchMapping("/{userID}/visibility")
    @Operation(summary = "Actualizar visibilidad", description = "Cambia la visibilidad publica de la cuenta.")
    public UserResponse updateVisibility(@PathVariable UUID userID, @Valid @RequestBody UpdateUserVisibilityRequest request) {
        return userService.updateVisibility(userID, request);
    }

    @DeleteMapping("/{userID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar usuario", description = "Elimina fisicamente un usuario durante el MVP.")
    public void deleteUser(@PathVariable UUID userID) {
        userService.deleteUser(userID);
    }
}
