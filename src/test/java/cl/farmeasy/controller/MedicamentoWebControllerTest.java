package cl.farmeasy.controller;

import cl.farmeasy.config.SecurityConfig;
import cl.farmeasy.config.ViewNames;
import cl.farmeasy.model.Medicamento;
import cl.farmeasy.security.UserDetailsServiceImpl;
import cl.farmeasy.service.FavoritoService;
import cl.farmeasy.service.HistorialBusquedaService;
import cl.farmeasy.service.MedicamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para el controlador web de medicamentos (MedicamentoWebController).
 * Verifica la logica de visualizacion del catalogo y los detalles de los medicamentos.
 */
@WebMvcTest(MedicamentoWebController.class)
@Import(SecurityConfig.class)
class MedicamentoWebControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MedicamentoService medicamentoService;
    @MockBean
    private FavoritoService favoritoService;
    @MockBean
    private HistorialBusquedaService historialBusquedaService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private Medicamento medicamento;

    /**
     * Prepara un objeto Medicamento de prueba antes de cada test.
     */
    @BeforeEach
    void setUp() {
        medicamento = new Medicamento();
        medicamento.setId("med1");
        medicamento.setNombre("Test-Med");
    }

    /**
     * Verifica que al pedir el catalogo sin busqueda se devuelvan todos los medicamentos.
     * @throws Exception si ocurre un error durante la peticion.
     */
    @Test
    void cuandoPideCatalogo_sinBusqueda_debeDevolverTodosLosMedicamentos() throws Exception {
        when(medicamentoService.obtenerTodos()).thenReturn(List.of(medicamento));
        when(favoritoService.obtenerIdsMedicamentosFavoritos(any())).thenReturn(Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders.get(ViewNames.CATALOGO_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.CATALOGO_VIEW))
                .andExpect(model().attributeExists("medicamentos"));

        verify(medicamentoService).obtenerTodos();
    }

    /**
     * Verifica que al buscar en el catalogo se llame al servicio de busqueda
     * y se guarde la busqueda en el historial.
     * @throws Exception si ocurre un error durante la peticion.
     */
    @Test
    @WithMockUser(username = "testuser")
    void cuandoPideCatalogo_conBusqueda_debeBuscarPorNombreYGuardarHistorial() throws Exception {
        String terminoBusqueda = "Test";
        when(medicamentoService.buscarPorNombre(terminoBusqueda)).thenReturn(List.of(medicamento));
        when(favoritoService.obtenerIdsMedicamentosFavoritos("testuser")).thenReturn(Set.of("med1"));

        mockMvc.perform(get(ViewNames.CATALOGO_URL).param("busqueda", terminoBusqueda))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.CATALOGO_VIEW))
                .andExpect(model().attributeExists("busquedaActual", "medicamentos", "favoritosIds"));

        verify(medicamentoService).buscarPorNombre(terminoBusqueda);
        verify(historialBusquedaService).guardarBusqueda(terminoBusqueda, "testuser");
    }

    /**
     * Verifica que la pagina de detalle de un medicamento se muestra correctamente
     * con un ID valido.
     * @throws Exception si ocurre un error durante la peticion.
     */
    @Test
    @WithMockUser(username = "testuser")
    void cuandoPideDetalle_conIdValido_debeDevolverPaginaDeDetalle() throws Exception {
        when(medicamentoService.findById("med1")).thenReturn(Optional.of(medicamento));
        when(medicamentoService.buscarPorNombre("Test-Med")).thenReturn(List.of(medicamento));
        when(favoritoService.esFavorito("testuser", "med1")).thenReturn(true);

        mockMvc.perform(get(ViewNames.MEDICAMENTOS_URL + "/{id}", "med1"))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.DETALLE_MEDICAMENTO_VIEW))
                .andExpect(model().attribute("medicamento", medicamento));
    }
}