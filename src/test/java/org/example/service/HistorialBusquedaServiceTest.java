package org.example.service;

import org.example.model.HistorialBusqueda;
import org.example.model.Usuario;
import org.example.repository.HistorialBusquedaRepository;
import org.example.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistorialBusquedaServiceTest {

    @Mock
    private HistorialBusquedaRepository historialRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private HistorialBusquedaService historialService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId("user123");
        usuario.setUsername("testuser");
    }

    // --- Tests para guardarBusqueda ---

    @Test
    void cuandoGuardaBusqueda_conTerminoValido_debeGuardarEnRepositorio() {
        // Arrange
        String termino = "Aspirina";
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(historialRepository.save(any(HistorialBusqueda.class))).thenReturn(new HistorialBusqueda());

        // Act
        historialService.guardarBusqueda(termino, "testuser");

        // Assert
        // Verificamos que se llamó al método save del repositorio exactamente una vez.
        verify(historialRepository, times(1)).save(any(HistorialBusqueda.class));
    }

    @Test
    void cuandoGuardaBusqueda_conTerminoNuloOVacio_noDebeGuardar() {
        // Act
        historialService.guardarBusqueda(null, "testuser");
        historialService.guardarBusqueda("   ", "testuser"); // Espacios en blanco

        // Assert
        // Verificamos que el método save NUNCA fue llamado.
        verify(historialRepository, never()).save(any(HistorialBusqueda.class));
    }

    @Test
    void cuandoGuardaBusqueda_conUsuarioInexistente_debeLanzarExcepcion() {
        // Arrange
        String termino = "Ibuprofeno";
        when(usuarioRepository.findByUsername("nouser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            historialService.guardarBusqueda(termino, "nouser");
        });
    }

    // --- Tests para obtenerHistorialPorUsuario ---

    @Test
    void cuandoObtieneHistorial_conUsuarioExistente_debeDevolverLista() {
        // Arrange
        HistorialBusqueda busqueda1 = new HistorialBusqueda("Aspirina", usuario);
        HistorialBusqueda busqueda2 = new HistorialBusqueda("Paracetamol", usuario);
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        // Simulamos que el repositorio devuelve una lista de historiales para ese usuario
        when(historialRepository.findByUsuarioId(anyString(), any(Sort.class)))
                .thenReturn(List.of(busqueda2, busqueda1)); // Simula el orden descendente

        // Act
        List<HistorialBusqueda> resultado = historialService.obtenerHistorialPorUsuario("testuser");

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Paracetamol", resultado.get(0).getTerminoBuscado()); // Verifica el orden
        verify(historialRepository).findByUsuarioId(eq("user123"), any(Sort.class));
    }

    @Test
    void cuandoObtieneHistorial_conUsuarioInexistente_debeLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.findByUsername("nouser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            historialService.obtenerHistorialPorUsuario("nouser");
        });
    }
}