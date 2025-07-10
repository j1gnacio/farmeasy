package cl.farmeasy.service;

import cl.farmeasy.exception.RegistroException;
import cl.farmeasy.repository.UsuarioRepository;
import cl.farmeasy.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * Servicio para gestionar la logica de negocio de los usuarios.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor para inyectar las dependencias del servicio de usuario.
     *
     * @param usuarioRepository El repositorio para el acceso a datos de usuarios.
     * @param passwordEncoder El codificador para las contrasenas.
     */
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username El nombre de usuario a buscar.
     * @return Un Optional que contiene al usuario si se encuentra.
     */
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param usuario El usuario a registrar.
     * @return El usuario registrado.
     * @throws RegistroException si el nombre de usuario o el email ya existen.
     */
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new RegistroException("El nombre de usuario ya existe: " + usuario.getUsername());
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RegistroException("El email ya esta registrado: " + usuario.getEmail());
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(usuario.getUsername());
        nuevoUsuario.setEmail(usuario.getEmail());
        nuevoUsuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        nuevoUsuario.setRoles(Collections.singleton("ROLE_USER"));
        nuevoUsuario.setEnabled(true);

        return usuarioRepository.save(nuevoUsuario);
    }
}