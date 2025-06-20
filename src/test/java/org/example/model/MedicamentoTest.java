package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MedicamentoTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        Medicamento med = new Medicamento();

        // Act
        med.setId("med01");
        med.setNombre("Ibuprofeno");
        med.setDescripcion("Analgésico");
        med.setPrecioInternet(2500.0);
        med.setPrecioFarmacia(3000.0);
        med.setFarmacia("Salcobrand");
        med.setUrlProducto("http://example.com");
        med.setImagenUrl("http://example.com/img.jpg");

        // Assert
        assertEquals("med01", med.getId());
        assertEquals("Ibuprofeno", med.getNombre());
        assertEquals("Analgésico", med.getDescripcion());
        assertEquals(2500.0, med.getPrecioInternet());
        assertEquals("Salcobrand", med.getFarmacia());
        assertEquals("http://example.com", med.getUrl()); // Usando tu getter manual
        assertEquals("http://example.com/img.jpg", med.getImagenUrl());
    }

    @Test
    void testConstructores() {
        // Test NoArgsConstructor
        Medicamento medVacio = new Medicamento();
        assertNull(medVacio.getId());

        // Test AllArgsConstructor (generado por Lombok)
        Medicamento medLombok = new Medicamento("id1", "Aspirina", 1000.0, 1200.0, "desc", "url", "img", "farm");
        assertEquals("id1", medLombok.getId());

        // Test tu constructor personalizado
        // Nota: los nombres de los parámetros del constructor (PrecioInternet) rompen la convención camelCase.
        Medicamento medCustom = new Medicamento("id2", "Paracetamol", "desc2", 2000.0, 2200.0, "url2", "farm2");
        assertEquals("id2", medCustom.getId());
        assertEquals(2000.0, medCustom.getPrecioInternet());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        // Creamos dos objetos idénticos
        Medicamento med1 = new Medicamento("id1", "Aspirina", "desc", 1000.0, 1200.0, "url", "farm");
        Medicamento med2 = new Medicamento("id1", "Aspirina", "desc", 1000.0, 1200.0, "url", "farm");
        // Y uno diferente
        Medicamento med3 = new Medicamento("id2", "Ibuprofeno", "desc2", 2000.0, 2200.0, "url2", "farm2");

        // Assert
        // Verificamos que los métodos generados por @Data funcionan como se espera
        assertEquals(med1, med2); // Deben ser iguales
        assertNotEquals(med1, med3); // Deben ser diferentes

        assertEquals(med1.hashCode(), med2.hashCode());
        assertNotEquals(med1.hashCode(), med3.hashCode());

        // También es una buena práctica verificar la salida de toString
        assertNotNull(med1.toString());
    }
}