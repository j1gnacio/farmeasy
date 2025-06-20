package org.example.controller;

import org.example.config.SecurityConfig;
import org.example.config.ViewNames; // <-- Importamos las constantes
import org.example.model.Favorito;
import org.example.model.Medicamento;
import org.example.model.Usuario;
import org.example.security.UserDetailsServiceImpl;
import org.example.service.FavoritoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoritoController.class)
@Import(SecurityConfig.class)
class FavoritoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FavoritoService favoritoService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private Medicamento medicamento;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setUsername("testuser");
        medicamento = new Medicamento();
        medicamento.setId("med1");
    }

    @Test
    @WithMockUser(username = "testuser")
    void cuandoUsuarioLogueadoPideFavoritos_debeMostrarVistaDeLista() throws Exception {
        when(favoritoService.obtenerFavoritosPorUsuario("testuser")).thenReturn(List.of(new Favorito(usuario, medicamento)));
        mockMvc.perform(get(ViewNames.FAVORITOS_URL)) // <-- CORREGIDO
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.FAVORITOS_VIEW)) // <-- CORREGIDO
                .andExpect(model().attributeExists("medicamentos"));
        verify(favoritoService).obtenerFavoritosPorUsuario("testuser");
    }

    @Test
    void cuandoUsuarioNoLogueadoPideFavoritos_debeRedirigirALogin() throws Exception {
        mockMvc.perform(get(ViewNames.FAVORITOS_URL)) // <-- CORREGIDO
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void cuandoUsuarioLogueadoAgregaFavorito_debeRedirigirACatalogoConMensaje() throws Exception {
        doNothing().when(favoritoService).agregarFavorito("testuser", "med1");
        mockMvc.perform(post(ViewNames.FAVORITOS_URL + "/agregar") // <-- CORREGIDO
                        .param("medicamentoId", "med1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ViewNames.CATALOGO_URL)) // <-- CORREGIDO
                .andExpect(flash().attribute("successMessage", "Medicamento añadido a favoritos."));
        verify(favoritoService).agregarFavorito("testuser", "med1");
    }

    @Test
    @WithMockUser(username = "testuser")
    void cuandoAgregarFavoritoFalla_debeRedirigirConMensajeDeError() throws Exception {
        String mensajeError = "Este medicamento ya está en tus favoritos.";
        doThrow(new IllegalStateException(mensajeError)).when(favoritoService).agregarFavorito(anyString(), anyString());
        mockMvc.perform(post(ViewNames.FAVORITOS_URL + "/agregar") // <-- CORREGIDO
                        .param("medicamentoId", "med1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ViewNames.CATALOGO_URL)) // <-- CORREGIDO
                .andExpect(flash().attribute("errorMessage", "Error al añadir a favoritos: " + mensajeError));
    }

    @Test
    @WithMockUser(username = "testuser")
    void cuandoEliminaFavorito_desdeCatalogo_debeRedirigirACatalogo() throws Exception {
        doNothing().when(favoritoService).eliminarFavorito("testuser", "med1");
        mockMvc.perform(post(ViewNames.FAVORITOS_URL + "/eliminar") // <-- CORREGIDO
                        .param("medicamentoId", "med1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ViewNames.CATALOGO_URL)); // <-- CORREGIDO
        verify(favoritoService).eliminarFavorito("testuser", "med1");
    }

    @Test
    @WithMockUser(username = "testuser")
    void cuandoEliminaFavorito_desdeFavoritos_debeRedirigirAFavoritos() throws Exception {
        doNothing().when(favoritoService).eliminarFavorito("testuser", "med1");
        mockMvc.perform(post(ViewNames.FAVORITOS_URL + "/eliminar") // <-- CORREGIDO
                        .param("medicamentoId", "med1")
                        .param("source", "favoritos")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ViewNames.FAVORITOS_URL)); // <-- CORREGIDO
        verify(favoritoService).eliminarFavorito("testuser", "med1");
    }
}