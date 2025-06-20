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

    @Test
    void testEqualsAndHashCodeAndToString() {
        // Arrange
        // Creamos objetos base para las comparaciones
        Usuario usuario1 = new Usuario();
        usuario1.setId("user1");

        Usuario usuario2 = new Usuario();
        usuario2.setId("user2");

        Medicamento med1 = new Medicamento();
        med1.setId("med1");

        Medicamento med2 = new Medicamento();
        med2.setId("med2");

        // Creamos dos instancias de Favorito que deberían ser idénticas
        Favorito f1 = new Favorito(usuario1, med1);
        f1.setId("fav1");

        Favorito f2 = new Favorito(usuario1, med1);
        f2.setId("fav1");

        // Creamos instancias diferentes para las comparaciones negativas
        Favorito f3_otroUsuario = new Favorito(usuario2, med1);
        f3_otroUsuario.setId("fav1");

        Favorito f4_otroMed = new Favorito(usuario1, med2);
        f4_otroMed.setId("fav1");

        Favorito f5_otroId = new Favorito(usuario1, med1);
        f5_otroId.setId("fav2");

        // Assert

        // 1. Probar el método equals()
        assertEquals(f1, f2, "Dos favoritos con los mismos datos deben ser iguales");
        assertNotEquals(f1, f3_otroUsuario, "Favoritos con usuarios diferentes no deben ser iguales");
        assertNotEquals(f1, f4_otroMed, "Favoritos con medicamentos diferentes no deben ser iguales");
        assertNotEquals(f1, f5_otroId, "Favoritos con IDs diferentes no deben ser iguales");
        //assertNotEquals(f1, null, "Un favorito no debe ser igual a nulo");
        //assertNotEquals(f1, new Object(), "Un favorito no debe ser igual a un objeto de otro tipo");

        // 2. Probar el método hashCode()
        assertEquals(f1.hashCode(), f2.hashCode(), "El hashcode de dos objetos iguales debe ser el mismo");
        assertNotEquals(f1.hashCode(), f3_otroUsuario.hashCode(), "El hashcode de objetos diferentes debería ser diferente");

        // 3. Probar el método toString()
        // Simplemente verificamos que no es nulo y que contiene el nombre de la clase
        String f1String = f1.toString();
        assertNotNull(f1String);
        assertTrue(f1String.contains("Favorito"));
    }
}