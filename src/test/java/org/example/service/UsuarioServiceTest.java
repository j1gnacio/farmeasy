package org.example.service;

import org.example.model.Usuario;
import org.example.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para UsuarioService.
 * Verifica la logica de negocio para el registro y busqueda de usuarios.
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    /**
     * Prepara un usuario de prueba antes de cada test.
     */
    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId("1");
        usuario.setUsername("testuser");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
    }

    /**
     * Prueba que un nuevo usuario se registra correctamente.
     * @throws Exception si ocurre un error durante el registro.
     */
    @Test
    void cuandoRegistraUsuario_conDatosNuevos_debeGuardarCorrectamente() throws Exception {
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario usuarioGuardado = usuarioService.registrarUsuario(usuario);

        assertNotNull(usuarioGuardado);
        assertEquals("encodedPassword", usuarioGuardado.getPassword());
        assertTrue(usuarioGuardado.getRoles().contains("ROLE_USER"));
        assertTrue(usuarioGuardado.isEnabled());

        verify(usuarioRepository).save(any(Usuario.class));
    }

    /**
     * Prueba que se lanza una excepcion al registrar un usuario con un username existente.
     */
    @Test
    void cuandoRegistraUsuario_conUsernameExistente_debeLanzarExcepcion() {
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.registrarUsuario(usuario);
        });

        assertEquals("El nombre de usuario ya existe: testuser", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    /**
     * Prueba que se lanza una excepcion al registrar un usuario con un email existente.
     */
    @Test
    void cuandoRegistraUsuario_conEmailExistente_debeLanzarExcepcion() {
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(false);
        when(usuarioRepository.existsByEmail("test@example.com")).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.registrarUsuario(usuario);
        });

        assertEquals("El email ya está registrado: test@example.com", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    /**
     * Prueba que la busqueda por username devuelve el usuario correcto.
     */
    @Test
    void cuandoBuscaPorUsername_debeDevolverUsuario() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        Optional<Usuario> encontrado = usuarioService.findByUsername("testuser");

        assertTrue(encontrado.isPresent());
        assertEquals("testuser", encontrado.get().getUsername());
        verify(usuarioRepository).findByUsername("testuser");
    }
}