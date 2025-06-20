package org.example.controller;

import org.example.config.SecurityConfig;
import org.example.config.ViewNames; // <-- Importamos las constantes
import org.example.model.Medicamento;
import org.example.security.UserDetailsServiceImpl;
import org.example.service.FavoritoService;
import org.example.service.HistorialBusquedaService;
import org.example.service.MedicamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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

    @BeforeEach
    void setUp() {
        medicamento = new Medicamento();
        medicamento.setId("med1");
        medicamento.setNombre("Test-Med");
    }

    // Este test fue omitido antes, lo añadimos para completar
    @Test
    void cuandoPideCatalogo_sinBusqueda_debeDevolverTodosLosMedicamentos() throws Exception {
        when(medicamentoService.obtenerTodos()).thenReturn(List.of(medicamento));
        when(favoritoService.obtenerIdsMedicamentosFavoritos(any())).thenReturn(Collections.emptySet());

        mockMvc.perform(get(ViewNames.CATALOGO_URL)) // <-- CORREGIDO
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.CATALOGO_VIEW)) // <-- CORREGIDO
                .andExpect(model().attributeExists("medicamentos"));

        verify(medicamentoService).obtenerTodos();
    }

    @Test
    @WithMockUser(username = "testuser")
    void cuandoPideCatalogo_conBusqueda_debeBuscarPorNombreYGuardarHistorial() throws Exception {
        String terminoBusqueda = "Test";
        when(medicamentoService.buscarPorNombre(terminoBusqueda)).thenReturn(List.of(medicamento));
        when(favoritoService.obtenerIdsMedicamentosFavoritos("testuser")).thenReturn(Set.of("med1"));

        mockMvc.perform(get(ViewNames.CATALOGO_URL).param("busqueda", terminoBusqueda)) // <-- CORREGIDO
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.CATALOGO_VIEW)) // <-- CORREGIDO
                .andExpect(model().attributeExists("busquedaActual", "medicamentos", "favoritosIds"));

        verify(medicamentoService).buscarPorNombre(terminoBusqueda);
        verify(historialBusquedaService).guardarBusqueda(terminoBusqueda, "testuser");
    }

    @Test
    @WithMockUser(username = "testuser")
    void cuandoPideDetalle_conIdValido_debeDevolverPaginaDeDetalle() throws Exception {
        when(medicamentoService.findById("med1")).thenReturn(Optional.of(medicamento));
        when(medicamentoService.buscarPorNombre("Test-Med")).thenReturn(List.of(medicamento));
        when(favoritoService.esFavorito("testuser", "med1")).thenReturn(true);

        mockMvc.perform(get(ViewNames.MEDICAMENTOS_URL + "/{id}", "med1")) // <-- CORREGIDO
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.DETALLE_MEDICAMENTO_VIEW)) // <-- CORREGIDO
                .andExpect(model().attribute("medicamento", medicamento));
    }
}