package org.example.service;

import org.example.model.Favorito;
import org.example.model.Medicamento;
import org.example.model.Usuario;
import org.example.repository.FavoritoRepository;
import org.example.repository.MedicamentoRepository;
import org.example.repository.UsuarioRepository;
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

    @BeforeEach
    void setUp() {
        // Preparamos los datos de prueba comunes
        usuario = new Usuario();
        usuario.setId("user123");
        usuario.setUsername("testuser");

        medicamento = new Medicamento();
        medicamento.setId("med456");
        medicamento.setNombre("Paracetamol");

        favorito = new Favorito(usuario, medicamento);
    }

    // --- Tests para agregarFavorito ---

    @Test
    void cuandoAgregaFavorito_conDatosValidos_debeGuardarCorrectamente() {
        // Arrange
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(medicamentoRepository.findById("med456")).thenReturn(Optional.of(medicamento));
        when(favoritoRepository.findByUsuarioIdAndMedicamentoId("user123", "med456")).thenReturn(Optional.empty()); // No es favorito aún

        // Act
        // El método no devuelve nada, así que solo lo llamamos. Si lanza una excepción, el test falla.
        assertDoesNotThrow(() -> favoritoService.agregarFavorito("testuser", "med456"));

        // Assert
        // Verificamos que los métodos de guardado fueron llamados
        verify(favoritoRepository).save(any(Favorito.class));
        verify(usuarioRepository).save(usuario); // Verifica que se actualiza la lista en el usuario
    }

    @Test
    void cuandoAgregaFavorito_queYaExiste_debeLanzarIllegalStateException() {
        // Arrange
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(medicamentoRepository.findById("med456")).thenReturn(Optional.of(medicamento));
        when(favoritoRepository.findByUsuarioIdAndMedicamentoId("user123", "med456")).thenReturn(Optional.of(favorito)); // Ya es favorito

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            favoritoService.agregarFavorito("testuser", "med456");
        });
        assertEquals("Este medicamento ya está en tus favoritos.", exception.getMessage());
        verify(favoritoRepository, never()).save(any(Favorito.class)); // No debe intentar guardar de nuevo
    }

    @Test
    void cuandoAgregaFavorito_conUsuarioInexistente_debeLanzarUsernameNotFoundException() {
        // Arrange
        when(usuarioRepository.findByUsername("nouser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            favoritoService.agregarFavorito("nouser", "med456");
        });
    }

    @Test
    void cuandoAgregaFavorito_conMedicamentoInexistente_debeLanzarNoSuchElementException() {
        // Arrange
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(medicamentoRepository.findById("nomed")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            favoritoService.agregarFavorito("testuser", "nomed");
        });
    }


    // --- Tests para eliminarFavorito ---

    @Test
    void cuandoEliminaFavorito_debeLlamarADeleteYSave() {
        // Arrange
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        // Act
        favoritoService.eliminarFavorito("testuser", "med456");

        // Assert
        verify(favoritoRepository).deleteByUsuarioIdAndMedicamentoId("user123", "med456");
        verify(usuarioRepository).save(usuario);
    }


    // --- Tests para obtenerFavoritosPorUsuario ---

    @Test
    void cuandoObtieneFavoritos_debeDevolverListaCorrecta() {
        // Arrange
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(favoritoRepository.findByUsuarioId("user123")).thenReturn(List.of(favorito));

        // Act
        List<Favorito> favoritos = favoritoService.obtenerFavoritosPorUsuario("testuser");

        // Assert
        assertFalse(favoritos.isEmpty());
        assertEquals(1, favoritos.size());
        assertEquals("med456", favoritos.get(0).getMedicamento().getId());
    }


    // --- Tests para esFavorito ---

    @Test
    void cuandoVerificaSiEsFavorito_yExiste_debeDevolverTrue() {
        // Arrange
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(favoritoRepository.findByUsuarioIdAndMedicamentoId("user123", "med456")).thenReturn(Optional.of(favorito));

        // Act
        boolean resultado = favoritoService.esFavorito("testuser", "med456");

        // Assert
        assertTrue(resultado);
    }

    @Test
    void cuandoVerificaSiEsFavorito_yNoExiste_debeDevolverFalse() {
        // Arrange
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(favoritoRepository.findByUsuarioIdAndMedicamentoId("user123", "med456")).thenReturn(Optional.empty());

        // Act
        boolean resultado = favoritoService.esFavorito("testuser", "med456");

        // Assert
        assertFalse(resultado);
    }

    // --- Tests para obtenerIdsMedicamentosFavoritos ---

    @Test
    void cuandoObtieneIdsDeFavoritos_debeDevolverSetDeIds() {
        // Arrange
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(favoritoRepository.findByUsuarioId("user123")).thenReturn(List.of(favorito));

        // Act
        Set<String> ids = favoritoService.obtenerIdsMedicamentosFavoritos("testuser");

        // Assert
        assertNotNull(ids);
        assertEquals(1, ids.size());
        assertTrue(ids.contains("med456"));
    }
}