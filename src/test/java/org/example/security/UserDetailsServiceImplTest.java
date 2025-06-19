package org.example.security;

import org.example.model.Usuario;
import org.example.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Preparamos un usuario de prueba
        usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setPassword("encodedPassword");
        usuario.setRoles(Set.of("ROLE_USER", "ROLE_ADMIN"));
        usuario.setEnabled(true);
    }

    // --- TEST 1: El caso de éxito, cuando el usuario es encontrado ---
    @Test
    void cuandoCargaUsuarioPorUsername_yUsuarioExiste_debeDevolverUserDetails() {
        // Arrange
        // Simulamos que el repositorio encuentra al usuario "testuser"
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        // Act
        // Llamamos al método que queremos probar
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Assert
        // Verificamos que los datos del UserDetails son los correctos
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());

        // Verificamos que tiene la cantidad correcta de roles/autoridades
        assertEquals(2, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER")));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")));
    }

    // --- TEST 2: El caso de error, cuando el usuario NO es encontrado ---
    @Test
    void cuandoCargaUsuarioPorUsername_yUsuarioNoExiste_debeLanzarUsernameNotFoundException() {
        // Arrange
        // Simulamos que el repositorio NO encuentra al usuario y devuelve un Optional vacío
        when(usuarioRepository.findByUsername("nouser")).thenReturn(Optional.empty());

        // Act & Assert
        // Verificamos que al llamar al método, se lanza la excepción esperada
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nouser");
        });

        // Verificamos que el mensaje de la excepción es el correcto
        assertEquals("Usuario no encontrado: nouser", exception.getMessage());
    }
}