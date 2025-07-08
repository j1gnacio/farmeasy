package org.example.repository;

import org.example.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repositorio de Spring Data MongoDB para la entidad Usuario.
 */
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username El nombre de usuario a buscar.
     * @return Un Optional que contiene al usuario si se encuentra.
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email El correo electrónico a buscar.
     * @return Un Optional que contiene al usuario si se encuentra.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica si existe un usuario con un nombre de usuario dado.
     *
     * @param username El nombre de usuario a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si existe un usuario con un correo electrónico dado.
     *
     * @param email El correo electrónico a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    boolean existsByEmail(String email);
}