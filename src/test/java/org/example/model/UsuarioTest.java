package org.example.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        Usuario usuario = new Usuario();

        // Act
        usuario.setId("user01");
        usuario.setUsername("testuser");
        usuario.setPassword("password123");
        usuario.setEmail("test@example.com");
        usuario.setRoles(Set.of("ROLE_USER"));
        usuario.setEnabled(true);
        usuario.setFavoritos(new ArrayList<>());

        // Assert
        assertEquals("user01", usuario.getId());
        assertEquals("testuser", usuario.getUsername());
        assertEquals("password123", usuario.getPassword());
        assertEquals("test@example.com", usuario.getEmail());
        assertTrue(usuario.getRoles().contains("ROLE_USER"));
        assertTrue(usuario.isEnabled());
        assertNotNull(usuario.getFavoritos());
    }

    @Test
    void testConstructores() {
        // Test NoArgsConstructor (ya probado implícitamente arriba)
        Usuario usuarioVacio = new Usuario();
        assertNull(usuarioVacio.getId());

        // Test AllArgsConstructor
        Usuario usuarioCompleto = new Usuario("user02", "fulluser", "pass", "full@example.com", new ArrayList<>(), new HashSet<>(), true);
        assertEquals("user02", usuarioCompleto.getId());
        assertEquals("fulluser", usuarioCompleto.getUsername());
    }

    @Test
    void testEqualsAndHashCodeAndToString() {
        // Arrange
        Usuario u1 = new Usuario("id1", "user", "pass", "email", new ArrayList<>(), Set.of(), true);
        Usuario u2 = new Usuario("id1", "user", "pass", "email", new ArrayList<>(), Set.of(), true);
        Usuario u3 = new Usuario("id2", "user3", "pass3", "email3", new ArrayList<>(), Set.of(), false);

        // Assert
        // Gracias a @Data de Lombok, estos métodos deberían funcionar correctamente.
        assertEquals(u1, u2); // Deben ser iguales porque tienen los mismos datos
        assertNotEquals(u1, u3); // Deben ser diferentes

        assertEquals(u1.hashCode(), u2.hashCode());
        assertNotEquals(u1.hashCode(), u3.hashCode());

        assertNotNull(u1.toString());
    }
}