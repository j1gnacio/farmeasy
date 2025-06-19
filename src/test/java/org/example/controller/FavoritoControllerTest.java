package org.example.controller;

import org.example.config.SecurityConfig;
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
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoritoController.class) // Enfocamos el test solo en FavoritoController
@Import(SecurityConfig.class)
class FavoritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoritoService favoritoService;

    // Los tests de controlador necesitan mocks de UserDetailsService para funcionar con la seguridad.
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

    // --- Tests para GET /favoritos ---

    @Test
    @WithMockUser(username = "testuser") // Simula un usuario logueado
    void cuandoUsuarioLogueadoPideFavoritos_debeMostrarVistaDeLista() throws Exception {
        // Arrange
        Favorito favorito = new Favorito(usuario, medicamento);
        when(favoritoService.obtenerFavoritosPorUsuario("testuser")).thenReturn(List.of(favorito));

        // Act & Assert
        mockMvc.perform(get("/favoritos"))
                .andExpect(status().isOk())
                .andExpect(view().name("favoritos/lista"))
                .andExpect(model().attributeExists("medicamentos"));

        verify(favoritoService).obtenerFavoritosPorUsuario("testuser");
    }

    @Test
    void cuandoUsuarioNoLogueadoPideFavoritos_debeRedirigirALogin() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/favoritos"))
                .andExpect(status().is3xxRedirection()) // Esperamos una redirección (código 302)
                .andExpect(redirectedUrlPattern("**/login")); // Verificamos que redirige a la página de login
    }

    // --- Tests para POST /favoritos/agregar ---

    @Test
    @WithMockUser(username = "testuser")
    void cuandoUsuarioLogueadoAgregaFavorito_debeRedirigirACatalogoConMensaje() throws Exception {
        // Arrange
        // doNothing() es para métodos 'void'. Le decimos que no haga nada (y no lance excepción) cuando se llame.
        doNothing().when(favoritoService).agregarFavorito("testuser", "med1");

        // Act & Assert
        mockMvc.perform(post("/favoritos/agregar")
                        .param("medicamentoId", "med1")
                        .with(csrf())) // IMPORTANTE: Las peticiones POST necesitan protección CSRF
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medicamentos/catalogo"))
                .andExpect(flash().attribute("successMessage", "Medicamento añadido a favoritos."));

        verify(favoritoService).agregarFavorito("testuser", "med1");
    }

    @Test
    @WithMockUser(username = "testuser")
    void cuandoAgregarFavoritoFalla_debeRedirigirConMensajeDeError() throws Exception {
        // Arrange
        String mensajeError = "Este medicamento ya está en tus favoritos.";
        // Simulamos que el servicio lanza una excepción
        doThrow(new IllegalStateException(mensajeError)).when(favoritoService).agregarFavorito(anyString(), anyString());

        // Act & Assert
        mockMvc.perform(post("/favoritos/agregar")
                        .param("medicamentoId", "med1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medicamentos/catalogo"))
                .andExpect(flash().attribute("errorMessage", "Error al añadir a favoritos: " + mensajeError));
    }


    // --- Tests para POST /favoritos/eliminar ---

    @Test
    @WithMockUser(username = "testuser")
    void cuandoEliminaFavorito_desdeCatalogo_debeRedirigirACatalogo() throws Exception {
        // Arrange
        doNothing().when(favoritoService).eliminarFavorito("testuser", "med1");

        // Act & Assert
        mockMvc.perform(post("/favoritos/eliminar")
                        .param("medicamentoId", "med1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medicamentos/catalogo"));

        verify(favoritoService).eliminarFavorito("testuser", "med1");
    }

    @Test
    @WithMockUser(username = "testuser")
    void cuandoEliminaFavorito_desdeFavoritos_debeRedirigirAFavoritos() throws Exception {
        // Arrange
        doNothing().when(favoritoService).eliminarFavorito("testuser", "med1");

        // Act & Assert
        // Simulamos el parámetro "source" que viene desde la página de favoritos
        mockMvc.perform(post("/favoritos/eliminar")
                        .param("medicamentoId", "med1")
                        .param("source", "favoritos")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/favoritos")); // Verificamos la redirección inteligente

        verify(favoritoService).eliminarFavorito("testuser", "med1");
    }
}