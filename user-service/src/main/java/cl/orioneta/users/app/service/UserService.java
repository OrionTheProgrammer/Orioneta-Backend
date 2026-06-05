package cl.orioneta.users.app.service;

import cl.orioneta.users.app.dto.CreateUserRequest;
import cl.orioneta.users.app.dto.UpdateUserProfileRequest;
import cl.orioneta.users.app.dto.UpdateUserStatusRequest;
import cl.orioneta.users.app.dto.UpdateUserVisibilityRequest;
import cl.orioneta.users.app.dto.UserResponse;
import cl.orioneta.users.app.repository.UserRepository;
import cl.orioneta.users.domain.exception.UserAlreadyExistsException;
import cl.orioneta.users.domain.exception.UserNotFoundException;
import cl.orioneta.users.domain.model.FriendCode;
import cl.orioneta.users.domain.model.User;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio principal de aplicacion para perfiles de usuario.
 *
 * <p>Esta clase coordina el flujo: valida duplicados, busca usuarios, llama a
 * metodos del dominio y guarda cambios. La regla practica es simple: si algo
 * es decision del negocio, va al dominio; si algo coordina pasos, queda aqui.</p>
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Crea un perfil publico nuevo.
     */
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        validateUserNameAvailable(request.userName());
        validateEmailAvailable(request.email());

        User user = createUserWithUniqueFriendCode(request);
        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    /**
     * Lista todos los usuarios registrados.
     */
    @Transactional(readOnly = true)
    public List<UserResponse> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    /**
     * Busca un usuario por su identificador interno.
     */
    @Transactional(readOnly = true)
    public UserResponse findUserById(UUID userID) {
        return userMapper.toResponse(findUserOrFail(userID));
    }

    /**
     * Busca un usuario por username.
     */
    @Transactional(readOnly = true)
    public UserResponse findUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ese username"));
    }

    /**
     * Busca un usuario por email.
     */
    @Transactional(readOnly = true)
    public UserResponse findUserByEmail(String email) {
        return userRepository.findByEmail(normalizeEmail(email))
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ese email"));
    }

    /**
     * Busca un usuario por friend code publico.
     */
    @Transactional(readOnly = true)
    public UserResponse findUserByFriendCode(String friendCode) {
        String validCode = FriendCode.codeValidator(friendCode);

        return userRepository.findByFriendCode(validCode)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ese friend code"));
    }

    /**
     * Actualiza los datos editables del perfil.
     */
    @Transactional
    public UserResponse updateProfile(UUID userID, UpdateUserProfileRequest request) {
        User user = findUserOrFail(userID);
        user.updateProfile(request.displayName(), request.bio(), request.profilePhoto());

        return userMapper.toResponse(userRepository.save(user));
    }

    /**
     * Actualiza el estado de presencia del usuario.
     */
    @Transactional
    public UserResponse updateStatus(UUID userID, UpdateUserStatusRequest request) {
        User user = findUserOrFail(userID);
        user.changeStatus(request.status());

        return userMapper.toResponse(userRepository.save(user));
    }

    /**
     * Actualiza la visibilidad publica de la cuenta.
     */
    @Transactional
    public UserResponse updateVisibility(UUID userID, UpdateUserVisibilityRequest request) {
        User user = findUserOrFail(userID);
        user.changeVisibility(request.visibility());

        return userMapper.toResponse(userRepository.save(user));
    }

    /**
     * Elimina fisicamente el usuario.
     *
     * <p>Para el MVP basta con eliminacion fisica. Si mas adelante necesitas
     * historial, se puede cambiar a borrado logico agregando un estado o
     * {@code deletedAt} en el dominio.</p>
     */
    @Transactional
    public void deleteUser(UUID userID) {
        findUserOrFail(userID);
        userRepository.deleteById(userID);
    }

    private User findUserOrFail(UUID userID) {
        return userRepository.findById(userID)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
    }

    private void validateUserNameAvailable(String userName) {
        if (userRepository.existsByUserName(userName)) {
            throw new UserAlreadyExistsException("El username ya esta en uso");
        }
    }

    private void validateEmailAvailable(String email) {
        if (userRepository.existsByEmail(normalizeEmail(email))) {
            throw new UserAlreadyExistsException("El email ya esta en uso");
        }
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            return null;
        }

        return email.trim().toLowerCase(Locale.ROOT);
    }

    private User createUserWithUniqueFriendCode(CreateUserRequest request) {
        User user;

        do {
            user = new User(
                    request.userName(),
                    request.displayName(),
                    request.bio(),
                    request.email(),
                    null,
                    null,
                    request.profilePhoto()
            );
        } while (userRepository.existsByFriendCode(user.getFriendCode()));

        return user;
    }
}
