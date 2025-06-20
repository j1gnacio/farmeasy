package org.example.controller;

import org.example.config.SecurityConfig;
import org.example.config.ViewNames; // <-- Importamos las constantes
import org.example.model.HistorialBusqueda;
import org.example.security.UserDetailsServiceImpl;
import org.example.service.HistorialBusquedaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistorialController.class)
@Import(SecurityConfig.class)
class HistorialControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HistorialBusquedaService historialService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @WithMockUser(username = "testuser")
    void cuandoUsuarioLogueadoPideHistorial_debeMostrarVistaDeLista() throws Exception {
        when(historialService.obtenerHistorialPorUsuario("testuser")).thenReturn(List.of(new HistorialBusqueda("Paracetamol", null)));
        mockMvc.perform(get(ViewNames.HISTORIAL_URL)) // <-- CORREGIDO
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.HISTORIAL_VIEW)) // <-- CORREGIDO
                .andExpect(model().attributeExists("historial"));
        verify(historialService).obtenerHistorialPorUsuario("testuser");
    }

    @Test
    void cuandoUsuarioNoLogueadoPideHistorial_debeRedirigirALogin() throws Exception {
        mockMvc.perform(get(ViewNames.HISTORIAL_URL)) // <-- CORREGIDO
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}