package cl.farmeasy.service;

import cl.farmeasy.model.Favorito;
import cl.farmeasy.model.Medicamento;
import cl.farmeasy.model.Usuario;
import cl.farmeasy.repository.FavoritoRepository;
import cl.farmeasy.repository.MedicamentoRepository;
import cl.farmeasy.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para FavoritoService.
 * Verifica la logica de negocio para agregar, eliminar y consultar favoritos.
 */
@ExtendWith(MockitoExtension.class)
class FavoritoServiceTest {

    @Mock
    private FavoritoRepository favoritoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private MedicamentoRepository medicamentoRepository;

    @InjectMocks
    private FavoritoService favoritoService;

    private Usuario usuario;
    private Medicamento medicamento;
    private Favorito favorito;

    /**
     * Configuracion inicial para cada prueba.
     */
    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId("user123");
        usuario.setUsername("testuser");

        medicamento = new Medicamento();
        medicamento.setId("med456");
        medicamento.setNombre("Paracetamol");

        favorito = new Favorito(usuario, medicamento);
    }

    /**
     * Prueba que un favorito se guarda correctamente con datos validos.
     */
    @Test
    void cuandoAgregaFavorito_conDatosValidos_debeGuardarCorrectamente() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(medicamentoRepository.findById("med456")).thenReturn(Optional.of(medicamento));
        when(favoritoRepository.findByUsuarioIdAndMedicamentoId("user123", "med456")).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> favoritoService.agregarFavorito("testuser", "med456"));

        verify(favoritoRepository).save(any(Favorito.class));
        verify(usuarioRepository).save(usuario);
    }

    /**
     * Prueba que se lanza una excepcion al intentar agregar un favorito que ya existe.
     */
    @Test
    void cuandoAgregaFavorito_queYaExiste_debeLanzarIllegalStateException() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(medicamentoRepository.findById("med456")).thenReturn(Optional.of(medicamento));
        when(favoritoRepository.findByUsuarioIdAndMedicamentoId("user123", "med456")).thenReturn(Optional.of(favorito));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            favoritoService.agregarFavorito("testuser", "med456");
        });
        assertEquals("Este medicamento ya está en tus favoritos.", exception.getMessage());
        verify(favoritoRepository, never()).save(any(Favorito.class));
    }

    /**
     * Prueba que se lanza una excepcion al agregar un favorito con un usuario que no existe.
     */
    @Test
    void cuandoAgregaFavorito_conUsuarioInexistente_debeLanzarUsernameNotFoundException() {
        when(usuarioRepository.findByUsername("nouser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            favoritoService.agregarFavorito("nouser", "med456");
        });
    }

    /**
     * Prueba que se lanza una excepcion al agregar un favorito con un medicamento que no existe.
     */
    @Test
    void cuandoAgregaFavorito_conMedicamentoInexistente_debeLanzarNoSuchElementException() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(medicamentoRepository.findById("nomed")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            favoritoService.agregarFavorito("testuser", "nomed");
        });
    }

    /**
     * Prueba la eliminacion de un favorito.
     */
    @Test
    void cuandoEliminaFavorito_debeLlamarADeleteYSave() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        favoritoService.eliminarFavorito("testuser", "med456");

        verify(favoritoRepository).deleteByUsuarioIdAndMedicamentoId("user123", "med456");
        verify(usuarioRepository).save(usuario);
    }

    /**
     * Prueba que se obtiene la lista correcta de favoritos para un usuario.
     */
    @Test
    void cuandoObtieneFavoritos_debeDevolverListaCorrecta() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(favoritoRepository.findByUsuarioId("user123")).thenReturn(List.of(favorito));

        List<Favorito> favoritos = favoritoService.obtenerFavoritosPorUsuario("testuser");

        assertFalse(favoritos.isEmpty());
        assertEquals(1, favoritos.size());
        assertEquals("med456", favoritos.get(0).getMedicamento().getId());
    }

    /**
     * Prueba la verificacion de un favorito existente.
     */
    @Test
    void cuandoVerificaSiEsFavorito_yExiste_debeDevolverTrue() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(favoritoRepository.findByUsuarioIdAndMedicamentoId("user123", "med456")).thenReturn(Optional.of(favorito));

        boolean resultado = favoritoService.esFavorito("testuser", "med456");

        assertTrue(resultado);
    }

    /**
     * Prueba la verificacion de un favorito que no existe.
     */
    @Test
    void cuandoVerificaSiEsFavorito_yNoExiste_debeDevolverFalse() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(favoritoRepository.findByUsuarioIdAndMedicamentoId("user123", "med456")).thenReturn(Optional.empty());

        boolean resultado = favoritoService.esFavorito("testuser", "med456");

        assertFalse(resultado);
    }

    /**
     * Prueba que se obtienen los IDs correctos de los medicamentos favoritos.
     */
    @Test
    void cuandoObtieneIdsDeFavoritos_debeDevolverSetDeIds() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(favoritoRepository.findByUsuarioId("user123")).thenReturn(List.of(favorito));

        Set<String> ids = favoritoService.obtenerIdsMedicamentosFavoritos("testuser");

        assertNotNull(ids);
        assertEquals(1, ids.size());
        assertTrue(ids.contains("med456"));
    }
}