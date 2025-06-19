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

@ExtendWith(MockitoExtension.class) // Habilita el uso de anotaciones de Mockito como @Mock y @InjectMocks
class UsuarioServiceTest {

    @Mock // Crea un objeto simulado (mock) de UsuarioRepository. No tocará la base de datos real.
    private UsuarioRepository usuarioRepository;

    @Mock // Crea un mock del PasswordEncoder.
    private PasswordEncoder passwordEncoder;

    @InjectMocks // Crea una instancia real de UsuarioService e inyecta los mocks de arriba.
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach // Este método se ejecuta antes de CADA test. Es útil para preparar datos comunes.
    void setUp() {
        // Creamos un objeto Usuario de prueba que usaremos en varios tests.
        usuario = new Usuario();
        usuario.setId("1");
        usuario.setUsername("testuser");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
    }

    // --- TEST 1: El Camino Feliz (Registro Exitoso) ---
    @Test
    void cuandoRegistraUsuario_conDatosNuevos_debeGuardarCorrectamente() throws Exception {
        // Arrange
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // --- LÍNEA AJUSTADA ---
        // Le decimos al mock: "Cuando te llamen con CUALQUIER objeto Usuario, devuelve ESE MISMO objeto".
        // Esto es más realista para simular el comportamiento de save.
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario usuarioGuardado = usuarioService.registrarUsuario(usuario);

        // Assert
        assertNotNull(usuarioGuardado);
        assertEquals("encodedPassword", usuarioGuardado.getPassword()); // ¡Ahora esta aserción pasará!
        assertTrue(usuarioGuardado.getRoles().contains("ROLE_USER"));
        assertTrue(usuarioGuardado.isEnabled());

        verify(usuarioRepository).save(any(Usuario.class));
    }

    // --- TEST 2: Camino de Error (Username ya existe) ---
    @Test
    void cuandoRegistraUsuario_conUsernameExistente_debeLanzarExcepcion() {
        // Arrange
        // Simulamos que el username SÍ existe.
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(true);

        // Act & Assert
        // Verificamos que al ejecutar el método, se lanza la excepción esperada.
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.registrarUsuario(usuario);
        });

        // Verificamos que el mensaje de la excepción es el correcto.
        assertEquals("El nombre de usuario ya existe: testuser", exception.getMessage());

        // Verificamos que el método save NUNCA fue llamado.
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // --- TEST 3: Camino de Error (Email ya existe) ---
    @Test
    void cuandoRegistraUsuario_conEmailExistente_debeLanzarExcepcion() {
        // Arrange
        // Simulamos que el username NO existe, pero el email SÍ.
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(false);
        when(usuarioRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.registrarUsuario(usuario);
        });

        assertEquals("El email ya está registrado: test@example.com", exception.getMessage());

        // Verificamos que el método save NUNCA fue llamado.
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // --- TEST 4: Prueba para el otro método del servicio ---
    @Test
    void cuandoBuscaPorUsername_debeDevolverUsuario() {
        // Arrange
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> encontrado = usuarioService.findByUsername("testuser");

        // Assert
        assertTrue(encontrado.isPresent());
        assertEquals("testuser", encontrado.get().getUsername());
        verify(usuarioRepository).findByUsername("testuser");
    }
}