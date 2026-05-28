package cl.orioneta.users.infrastructure.in.web;

import cl.orioneta.users.application.dto.CreateUserRequestDTO;
import cl.orioneta.users.application.dto.UpdateUserProfileRequestDTO;
import cl.orioneta.users.application.dto.UpdateUserStatusRequestDTO;
import cl.orioneta.users.application.dto.UserResponseDTO;
import cl.orioneta.users.application.mapper.UserMapper;
import cl.orioneta.users.application.usecase.CreateUserUseCase;
import cl.orioneta.users.application.usecase.DeleteUserUseCase;
import cl.orioneta.users.application.usecase.FindAllUsersUseCase;
import cl.orioneta.users.application.usecase.FindUserByFriendCodeUseCase;
import cl.orioneta.users.application.usecase.FindUserByIdUseCase;
import cl.orioneta.users.application.usecase.UpdateUserProfileUseCase;
import cl.orioneta.users.application.usecase.UpdateUserStatusUseCase;
import cl.orioneta.users.domain.model.User;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para perfiles publicos de usuario.
 *
 * <p>El controlador solo adapta HTTP a casos de uso. La logica de negocio vive
 * en la capa de aplicacion y el mapeo de salida se delega en {@link UserMapper}.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final FindUserByFriendCodeUseCase findUserByFriendCodeUseCase;
    private final FindAllUsersUseCase findAllUsersUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final UpdateUserStatusUseCase updateUserStatusUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UserMapper userMapper;

    /**
     * Crea el controlador con los casos de uso necesarios para el MVP.
     *
     * @param createUserUseCase caso de uso de creacion
     * @param findUserByIdUseCase caso de uso de busqueda por id
     * @param findUserByFriendCodeUseCase caso de uso de busqueda por friend code
     * @param findAllUsersUseCase caso de uso de listado
     * @param updateUserProfileUseCase caso de uso de actualizacion de perfil
     * @param updateUserStatusUseCase caso de uso de actualizacion de estado
     * @param deleteUserUseCase caso de uso de eliminacion
     * @param userMapper mapper de dominio a DTO
     */
    public UserController(
            CreateUserUseCase createUserUseCase,
            FindUserByIdUseCase findUserByIdUseCase,
            FindUserByFriendCodeUseCase findUserByFriendCodeUseCase,
            FindAllUsersUseCase findAllUsersUseCase,
            UpdateUserProfileUseCase updateUserProfileUseCase,
            UpdateUserStatusUseCase updateUserStatusUseCase,
            DeleteUserUseCase deleteUserUseCase,
            UserMapper userMapper
    ) {
        this.createUserUseCase = createUserUseCase;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.findUserByFriendCodeUseCase = findUserByFriendCodeUseCase;
        this.findAllUsersUseCase = findAllUsersUseCase;
        this.updateUserProfileUseCase = updateUserProfileUseCase;
        this.updateUserStatusUseCase = updateUserStatusUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.userMapper = userMapper;
    }

    /**
     * Crea un perfil publico de usuario.
     *
     * @param request datos iniciales del usuario
     * @return usuario creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(@Valid @RequestBody CreateUserRequestDTO request) {
        User user = createUserUseCase.execute(request);
        return userMapper.toResponse(user);
    }

    /**
     * Lista todos los usuarios disponibles para el MVP.
     *
     * @return usuarios mapeados a DTO
     */
    @GetMapping
    public List<UserResponseDTO> findAllUsers() {
        return findAllUsersUseCase.execute()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    /**
     * Busca un usuario por id interno.
     *
     * @param id id del usuario
     * @return usuario encontrado
     */
    @GetMapping("/{id}")
    public UserResponseDTO findUserById(@PathVariable UUID id) {
        User user = findUserByIdUseCase.execute(id);
        return userMapper.toResponse(user);
    }

    /**
     * Busca un usuario por friend code.
     *
     * @param friendCode codigo publico de amistad
     * @return usuario encontrado
     */
    @GetMapping("/friend-code/{friendCode}")
    public UserResponseDTO findUserByFriendCode(@PathVariable String friendCode) {
        User user = findUserByFriendCodeUseCase.execute(friendCode);
        return userMapper.toResponse(user);
    }

    /**
     * Actualiza los datos editables del perfil.
     *
     * @param id id del usuario
     * @param request nuevos datos del perfil
     * @return usuario actualizado
     */
    @PatchMapping("/{id}/profile")
    public UserResponseDTO updateProfile(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserProfileRequestDTO request
    ) {
        User user = updateUserProfileUseCase.execute(id, request);
        return userMapper.toResponse(user);
    }

    /**
     * Actualiza el estado de presencia.
     *
     * @param id id del usuario
     * @param request nuevo estado
     * @return usuario actualizado
     */
    @PatchMapping("/{id}/status")
    public UserResponseDTO updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserStatusRequestDTO request
    ) {
        User user = updateUserStatusUseCase.execute(id, request);
        return userMapper.toResponse(user);
    }

    /**
     * Elimina un usuario existente.
     *
     * @param id id del usuario
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID id) {
        deleteUserUseCase.execute(id);
    }
}
