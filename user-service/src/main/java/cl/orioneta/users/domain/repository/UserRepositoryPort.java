package cl.orioneta.users.domain.repository;

import java.util.List;
import java.util.Optional;

import cl.orioneta.users.domain.model.User;
import cl.orioneta.users.domain.model.UserID;

/**
 * Puerto de salida usado por los casos de uso para persistir y buscar usuarios.
 *
 * <p>Las capas de dominio y aplicacion dependen de esta interfaz en vez de JPA.
 * Los adaptadores de infraestructura pueden implementarla con repositorios,
 * clientes HTTP o dobles de prueba sin obligar al negocio a conocer esos
 * detalles.
 */
public interface UserRepositoryPort {

    /**
     * Guarda un agregado de usuario.
     *
     * @param user usuario a guardar
     * @return usuario guardado
     */
    User save(User user);

    /**
     * Busca un usuario por id interno.
     *
     * @param id id interno del usuario
     * @return usuario encontrado, si existe
     */
    Optional<User> findById(UserID id);

    /**
     * Busca un usuario por username unico.
     *
     * @param username username a buscar
     * @return usuario encontrado, si existe
     */
    Optional<User> findByUsername(String username);

    /**
     * Busca un usuario por correo unico.
     *
     * @param email correo a buscar
     * @return usuario encontrado, si existe
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca un usuario por codigo de amistad hexadecimal publico.
     *
     * @param friendCode codigo publico usado por flujos de amistad
     * @return usuario encontrado, si existe
     */
    Optional<User> findByFriendCode(String friendCode);

    /**
     * Lista todos los usuarios disponibles para el adaptador actual.
     *
     * @return usuarios encontrados
     */
    List<User> findAll();

    /**
     * Revisa si un username ya existe antes de crear un usuario.
     *
     * @param username username a revisar
     * @return true si el username ya existe
     */
    boolean existsByUsername(String username);

    /**
     * Revisa si un correo ya existe antes de crear un usuario.
     *
     * @param email correo a revisar
     * @return true si el correo ya existe
     */
    boolean existsByEmail(String email);

    /**
     * Revisa si un codigo de amistad ya existe antes de guardar uno generado.
     *
     * @param friendCode candidato de codigo de amistad
     * @return true si el codigo ya existe
     */
    boolean existsByFriendCode(String friendCode);

    /**
     * Elimina un usuario por id interno.
     *
     * @param id id del usuario a eliminar
     */
    void deleteById(UserID id);
}
