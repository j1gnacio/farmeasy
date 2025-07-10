package cl.farmeasy.service;

import cl.farmeasy.exception.RegistroException;
import cl.farmeasy.repository.UsuarioRepository;
import cl.farmeasy.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Cambia la contrasena de un usuario existente.
     *
     * @param username El nombre del usuario que cambia su contrasena.
     * @param oldPassword La contrasena actual para verificacion.
     * @param newPassword La nueva contrasena a establecer.
     * @throws RegistroException si la contrasena antigua no es correcta.
     */
    @Transactional
    public void cambiarPassword(String username, String oldPassword, String newPassword) {
        // Buscamos al usuario por su nombre de usuario
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Verificamos que la contrasena antigua coincida
        if (!passwordEncoder.matches(oldPassword, usuario.getPassword())) {
            throw new RegistroException("La contrasena actual es incorrecta.");
        }

        // Codificamos y establecemos la nueva contrasena
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
    }
}