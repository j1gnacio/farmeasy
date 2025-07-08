package org.example.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase de modelo HistorialBusqueda.
 * Verifica el constructor, getters, setters y la logica de negocio basica.
 */
class HistorialBusquedaTest {

    /**
     * Prueba el constructor y los getters de la clase.
     */
    @Test
    void testConstructorYGetters() {
        Usuario usuario = new Usuario();
        usuario.setId("user01");
        String termino = "Aspirina";

        HistorialBusqueda historial = new HistorialBusqueda(termino, usuario);

        assertEquals(termino, historial.getTerminoBuscado());
        assertEquals(usuario, historial.getUsuario());
        assertNotNull(historial.getFechaBusqueda());
        assertTrue(historial.getFechaBusqueda().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    /**
     * Prueba los setters de la clase.
     */
    @Test
    void testSetters() {
        HistorialBusqueda historial = new HistorialBusqueda();
        LocalDateTime fecha = LocalDateTime.of(2023, 1, 1, 12, 0);

        historial.setId("hist1");
        historial.setTerminoBuscado("Ibuprofeno");
        historial.setFechaBusqueda(fecha);
        historial.setUsuario(new Usuario());

        assertEquals("hist1", historial.getId());
        assertEquals("Ibuprofeno", historial.getTerminoBuscado());
        assertEquals(fecha, historial.getFechaBusqueda());
    }

    /**
     * Prueba la igualdad y el calculo de hash code entre objetos.
     */
    @Test
    void testEqualsAndHashCode() {
        Usuario u1 = new Usuario();
        u1.setId("user1");

        HistorialBusqueda h1 = new HistorialBusqueda("aspirina", u1);
        h1.setId("hist1");

        HistorialBusqueda h2 = new HistorialBusqueda("aspirina", u1);
        h2.setId("hist1");

        HistorialBusqueda h3 = new HistorialBusqueda("ibuprofeno", u1);
        h3.setId("hist2");

        assertEquals(h1, h2);
        assertNotEquals(h1, h3);

        assertEquals(h1.hashCode(), h2.hashCode());
        assertNotEquals(h1.hashCode(), h3.hashCode());
    }
}