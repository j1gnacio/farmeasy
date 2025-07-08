package cl.farmeasy.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase de modelo Medicamento.
 */
class MedicamentoTest {

    /**
     * Prueba los getters y setters basicos de la clase.
     */
    @Test
    void testGettersAndSetters() {
        Medicamento med = new Medicamento();

        med.setId("med01");
        med.setNombre("Ibuprofeno");
        med.setDescripcion("Analgesico");
        med.setPrecioInternet(2500.0);
        med.setPrecioFarmacia(3000.0);
        med.setFarmacia("Salcobrand");
        med.setUrlProducto("http://example.com");
        med.setImagenUrl("http://example.com/img.jpg");

        assertEquals("med01", med.getId());
        assertEquals("Ibuprofeno", med.getNombre());
        assertEquals("Analgesico", med.getDescripcion());
        assertEquals(2500.0, med.getPrecioInternet());
        assertEquals("Salcobrand", med.getFarmacia());
        assertEquals("http://example.com", med.getUrl());
        assertEquals("http://example.com/img.jpg", med.getImagenUrl());
    }

    /**
     * Prueba los diferentes constructores de la clase.
     */
    @Test
    void testConstructores() {
        Medicamento medVacio = new Medicamento();
        assertNull(medVacio.getId());

        Medicamento medLombok = new Medicamento("id1", "Aspirina", 1000.0, 1200.0, "desc", "url", "img", "farm");
        assertEquals("id1", medLombok.getId());

        Medicamento medCustom = new Medicamento("id2", "Paracetamol", "desc2", 2000.0, 2200.0, "url2", "farm2");
        assertEquals("id2", medCustom.getId());
        assertEquals(2000.0, medCustom.getPrecioInternet());
    }

    /**
     * Prueba la logica de igualdad y hash code entre objetos.
     */
    @Test
    void testEqualsAndHashCode() {
        Medicamento med1 = new Medicamento("id1", "Aspirina", "desc", 1000.0, 1200.0, "url", "farm");
        Medicamento med2 = new Medicamento("id1", "Aspirina", "desc", 1000.0, 1200.0, "url", "farm");
        Medicamento med3 = new Medicamento("id2", "Ibuprofeno", "desc2", 2000.0, 2200.0, "url2", "farm2");

        assertEquals(med1, med2);
        assertNotEquals(med1, med3);

        assertEquals(med1.hashCode(), med2.hashCode());
        assertNotEquals(med1.hashCode(), med3.hashCode());

        assertNotNull(med1.toString());
    }
}