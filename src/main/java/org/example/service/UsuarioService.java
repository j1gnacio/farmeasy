package org.example.service;

import org.example.exception.RegistroException; // <-- Importación nueva
import org.example.model.Usuario;
import org.example.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Usuario registrarUsuario(Usuario usuario) { // Ya no es necesario 'throws Exception'
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            // Lanza la nueva excepción específica
            throw new RegistroException("El nombre de usuario ya existe: " + usuario.getUsername());
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            // Lanza la nueva excepción específica
            throw new RegistroException("El email ya está registrado: " + usuario.getEmail());
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