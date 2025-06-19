package org.example.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class HistorialBusquedaTest {

    @Test
    void testConstructorYGetters() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId("user01");
        String termino = "Aspirina";

        // Act
        // Usamos el constructor público para crear la instancia
        HistorialBusqueda historial = new HistorialBusqueda(termino, usuario);

        // Assert
//        assertNotNull(historial.getId()); // El ID se autogenera en el modelo, así que no debe ser nulo
        assertEquals(termino, historial.getTerminoBuscado());
        assertEquals(usuario, historial.getUsuario());
        assertNotNull(historial.getFechaBusqueda());
        // Verificamos que la fecha sea muy reciente
        assertTrue(historial.getFechaBusqueda().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testSetters() {
        // Arrange
        HistorialBusqueda historial = new HistorialBusqueda();
        LocalDateTime fecha = LocalDateTime.of(2023, 1, 1, 12, 0);

        // Act
        historial.setId("hist1");
        historial.setTerminoBuscado("Ibuprofeno");
        historial.setFechaBusqueda(fecha);
        historial.setUsuario(new Usuario());

        // Assert
        assertEquals("hist1", historial.getId());
        assertEquals("Ibuprofeno", historial.getTerminoBuscado());
        assertEquals(fecha, historial.getFechaBusqueda());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Usuario u1 = new Usuario();
        u1.setId("user1");

        HistorialBusqueda h1 = new HistorialBusqueda("aspirina", u1);
        h1.setId("hist1");

        HistorialBusqueda h2 = new HistorialBusqueda("aspirina", u1);
        h2.setId("hist1");

        HistorialBusqueda h3 = new HistorialBusqueda("ibuprofeno", u1);
        h3.setId("hist2");

        // Assert
        assertEquals(h1, h2);
        assertNotEquals(h1, h3);

        assertEquals(h1.hashCode(), h2.hashCode());
        assertNotEquals(h1.hashCode(), h3.hashCode());
    }

}