package cl.farmeasy.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase de modelo Usuario.
 */
class UsuarioTest {

    /**
     * Prueba los getters y setters de la clase Usuario.
     */
    @Test
    void testGettersAndSetters() {
        Usuario usuario = new Usuario();

        usuario.setId("user01");
        usuario.setUsername("testuser");
        usuario.setPassword("password123");
        usuario.setEmail("test@example.com");
        usuario.setRoles(Set.of("ROLE_USER"));
        usuario.setEnabled(true);
        usuario.setFavoritos(new ArrayList<>());

        assertEquals("user01", usuario.getId());
        assertEquals("testuser", usuario.getUsername());
        assertEquals("password123", usuario.getPassword());
        assertEquals("test@example.com", usuario.getEmail());
        assertTrue(usuario.getRoles().contains("ROLE_USER"));
        assertTrue(usuario.isEnabled());
        assertNotNull(usuario.getFavoritos());
    }

    /**
     * Prueba los constructores de la clase Usuario.
     */
    @Test
    void testConstructores() {
        Usuario usuarioVacio = new Usuario();
        assertNull(usuarioVacio.getId());

        Usuario usuarioCompleto = new Usuario("user02", "fulluser", "pass", "full@example.com", new ArrayList<>(), new HashSet<>(), true);
        assertEquals("user02", usuarioCompleto.getId());
        assertEquals("fulluser", usuarioCompleto.getUsername());
    }

    /**
     * Prueba la logica de equals, hashCode y toString.
     */
    @Test
    void testEqualsAndHashCodeAndToString() {
        Usuario u1 = new Usuario("id1", "user", "pass", "email", new ArrayList<>(), Set.of(), true);
        Usuario u2 = new Usuario("id1", "user", "pass", "email", new ArrayList<>(), Set.of(), true);
        Usuario u3 = new Usuario("id2", "user3", "pass3", "email3", new ArrayList<>(), Set.of(), false);

        assertEquals(u1, u2);
        assertNotEquals(u1, u3);

        assertEquals(u1.hashCode(), u2.hashCode());
        assertNotEquals(u1.hashCode(), u3.hashCode());

        assertNotNull(u1.toString());
    }
}