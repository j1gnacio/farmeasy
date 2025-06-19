package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FavoritoTest {

    @Test
    void testConstructorYGetters() {
        // Arrange
        Usuario usuario = new Usuario();
        Medicamento medicamento = new Medicamento();

        // Act
        Favorito favorito = new Favorito(usuario, medicamento);

        // Assert
        // 1. Verificamos el estado inicial: el ID DEBE ser nulo.
        assertNull(favorito.getId());
        assertEquals(usuario, favorito.getUsuario());
        assertEquals(medicamento, favorito.getMedicamento());
    }

    @Test
    void testSetters() {
        // Arrange
        Favorito favorito = new Favorito();

        // Act
        favorito.setId("fav1"); // Asignamos un ID manualmente
        favorito.setUsuario(new Usuario());
        favorito.setMedicamento(new Medicamento());

        // Assert
        // 2. Ahora que hemos usado el setter, el ID NO DEBE ser nulo.
        assertEquals("fav1", favorito.getId());
        assertNotNull(favorito.getUsuario());
        assertNotNull(favorito.getMedicamento());
    }
}