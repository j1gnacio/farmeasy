package cl.farmeasy.service;

import cl.farmeasy.model.HistorialBusqueda;
import cl.farmeasy.model.Usuario;
import cl.farmeasy.repository.HistorialBusquedaRepository;
import cl.farmeasy.repository.UsuarioRepository;
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

/**
 * Pruebas unitarias para HistorialBusquedaService.
 * Verifica la logica para guardar y obtener el historial de busqueda de los usuarios.
 */
@ExtendWith(MockitoExtension.class)
class HistorialBusquedaServiceTest {

    @Mock
    private HistorialBusquedaRepository historialRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private HistorialBusquedaService historialService;

    private Usuario usuario;

    /**
     * Prepara un usuario de prueba antes de cada test.
     */
    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId("user123");
        usuario.setUsername("testuser");
    }

    /**
     * Prueba que una busqueda con un termino valido se guarda correctamente.
     */
    @Test
    void cuandoGuardaBusqueda_conTerminoValido_debeGuardarEnRepositorio() {
        String termino = "Aspirina";
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(historialRepository.save(any(HistorialBusqueda.class))).thenReturn(new HistorialBusqueda());

        historialService.guardarBusqueda(termino, "testuser");

        verify(historialRepository, times(1)).save(any(HistorialBusqueda.class));
    }

    /**
     * Prueba que una busqueda con un termino nulo o vacio no se guarda.
     */
    @Test
    void cuandoGuardaBusqueda_conTerminoNuloOVacio_noDebeGuardar() {
        historialService.guardarBusqueda(null, "testuser");
        historialService.guardarBusqueda("   ", "testuser");

        verify(historialRepository, never()).save(any(HistorialBusqueda.class));
    }

    /**
     * Prueba que se lanza una excepcion al guardar una busqueda para un usuario inexistente.
     */
    @Test
    void cuandoGuardaBusqueda_conUsuarioInexistente_debeLanzarExcepcion() {
        String termino = "Ibuprofeno";
        when(usuarioRepository.findByUsername("nouser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            historialService.guardarBusqueda(termino, "nouser");
        });
    }

    /**
     * Prueba que se devuelve la lista de historial para un usuario existente.
     */
    @Test
    void cuandoObtieneHistorial_conUsuarioExistente_debeDevolverLista() {
        HistorialBusqueda busqueda1 = new HistorialBusqueda("Aspirina", usuario);
        HistorialBusqueda busqueda2 = new HistorialBusqueda("Paracetamol", usuario);
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(historialRepository.findByUsuarioId(anyString(), any(Sort.class)))
                .thenReturn(List.of(busqueda2, busqueda1));

        List<HistorialBusqueda> resultado = historialService.obtenerHistorialPorUsuario("testuser");

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Paracetamol", resultado.get(0).getTerminoBuscado());
        verify(historialRepository).findByUsuarioId(eq("user123"), any(Sort.class));
    }

    /**
     * Prueba que se lanza una excepcion al obtener el historial de un usuario inexistente.
     */
    @Test
    void cuandoObtieneHistorial_conUsuarioInexistente_debeLanzarExcepcion() {
        when(usuarioRepository.findByUsername("nouser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            historialService.obtenerHistorialPorUsuario("nouser");
        });
    }
}