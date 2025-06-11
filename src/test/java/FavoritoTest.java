import org.example.Favorito;
import org.example.Medicamento;
import org.example.Usuario;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class FavoritoTest {

    @Test
    public void testAgregarFavorito() {
        Usuario usuario = new Usuario("Luis");
        Medicamento medicamento = new Medicamento("1", "Omeprazol", "500mg", "Analgésico",
                "50", "45", "http://url.com", "Farmacia1");
        Favorito favorito = new Favorito(usuario, medicamento);

        usuario.agregarAFavoritos(favorito);

        assertTrue(usuario.getFavoritos().contains(favorito));
    }

    @Test
    public void testEliminarFavorito() {
        Usuario usuario = new Usuario("Marta");
        Medicamento medicamento = new Medicamento("2", "Amoxicilina", "500mg", "Antibiótico",
                "30", "28", "http://url.com", "Farmacia2");
        Favorito favorito = new Favorito(usuario, medicamento);

        usuario.agregarAFavoritos(favorito);
        usuario.eliminarDeFavoritos(favorito);

        assertFalse(usuario.getFavoritos().contains(favorito));
    }

    @Test
    public void testIdFavoritoSeGenera() {
        Usuario usuario = new Usuario("Pedro");
        Medicamento medicamento = new Medicamento("3", "Ibuprofeno", "200mg", "Analgésico",
                "40", "38", "http://url.com", "Farmacia3");
        Favorito favorito = new Favorito(usuario, medicamento);

        assertNotNull(favorito.getIdFavorito());
        assertTrue(favorito.getIdFavorito().startsWith("FAV-"));
    }

    @Test
    public void testGettersFavorito() {
        Usuario usuario = new Usuario("Ana");
        Medicamento medicamento = new Medicamento("4", "Paracetamol", "500mg", "Analgésico",
                "30", "28", "http://url.com", "Farmacia4");
        Favorito favorito = new Favorito(usuario, medicamento);

        assertEquals(usuario, favorito.getUsuario());
        assertEquals(medicamento, favorito.getMedicamento());
    }
}
