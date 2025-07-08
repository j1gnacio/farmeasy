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

/**
 * Pruebas unitarias para UserDetailsServiceImpl.
 * Verifica la logica de carga de detalles de usuario para Spring Security.
 */
@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private Usuario usuario;

    /**
     * Prepara un usuario de prueba antes de cada test.
     */
    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setPassword("encodedPassword");
        usuario.setRoles(Set.of("ROLE_USER", "ROLE_ADMIN"));
        usuario.setEnabled(true);
    }

    /**
     * Prueba que los detalles de un usuario existente se cargan correctamente.
     */
    @Test
    void cuandoCargaUsuarioPorUsername_yUsuarioExiste_debeDevolverUserDetails() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertEquals(2, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER")));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")));
    }

    /**
     * Prueba que se lanza una excepcion al intentar cargar un usuario que no existe.
     */
    @Test
    void cuandoCargaUsuarioPorUsername_yUsuarioNoExiste_debeLanzarUsernameNotFoundException() {
        when(usuarioRepository.findByUsername("nouser")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nouser");
        });

        assertEquals("Usuario no encontrado: nouser", exception.getMessage());
    }
}