package cl.farmeasy.controller;

import cl.farmeasy.config.SecurityConfig;
import cl.farmeasy.config.ViewNames;
import cl.farmeasy.model.Favorito;
import cl.farmeasy.model.Medicamento;
import cl.farmeasy.model.Usuario;
import cl.farmeasy.security.UserDetailsServiceImpl;
import cl.farmeasy.service.FavoritoService;
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

/**
 * Pruebas unitarias para el controlador de favoritos (FavoritoController).
 * Verifica el comportamiento de los endpoints para ver, agregar y eliminar favoritos.
 */
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

    /**
     * Prepara los datos de prueba antes de cada test.
     */
    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setUsername("testuser");
        medicamento = new Medicamento();
        medicamento.setId("med1");
    }

    /**
     * Verifica que un usuario logueado puede ver su lista de favoritos.
     * @throws Exception si ocurre un error durante la peticion.
     */
    @Test
    @WithMockUser(username = "testuser")
    void cuandoUsuarioLogueadoPideFavoritos_debeMostrarVistaDeLista() throws Exception {
        when(favoritoService.obtenerFavoritosPorUsuario("testuser")).thenReturn(List.of(new Favorito(usuario, medicamento)));
        mockMvc.perform(get(ViewNames.FAVORITOS_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.FAVORITOS_VIEW))
                .andExpect(model().attributeExists("medicamentos"));
        verify(favoritoService).obtenerFavoritosPorUsuario("testuser");
    }

    /**
     * Verifica que un usuario no logueado es redirigido a la pagina de login
     * al intentar acceder a sus favoritos.
     * @throws Exception si ocurre un error durante la peticion.
     */
    @Test
    void cuandoUsuarioNoLogueadoPideFavoritos_debeRedirigirALogin() throws Exception {
        mockMvc.perform(get(ViewNames.FAVORITOS_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    /**
     * Verifica que un usuario logueado puede agregar un favorito y es
     * redirigido al catalogo con un mensaje de exito.
     * @throws Exception si ocurre un error durante la peticion.
     */
    @Test
    @WithMockUser(username = "testuser")
    void cuandoUsuarioLogueadoAgregaFavorito_debeRedirigirACatalogoConMensaje() throws Exception {
        doNothing().when(favoritoService).agregarFavorito("testuser", "med1");
        mockMvc.perform(post(ViewNames.FAVORITOS_URL + "/agregar")
                        .param("medicamentoId", "med1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ViewNames.CATALOGO_URL))
                .andExpect(flash().attribute("successMessage", "Medicamento añadido a favoritos."));
        verify(favoritoService).agregarFavorito("testuser", "med1");
    }

    /**
     * Verifica que al fallar la adicion de un favorito, se redirige con
     * un mensaje de error.
     * @throws Exception si ocurre un error durante la peticion.
     */
    @Test
    @WithMockUser(username = "testuser")
    void cuandoAgregarFavoritoFalla_debeRedirigirConMensajeDeError() throws Exception {
        String mensajeError = "Este medicamento ya está en tus favoritos.";
        doThrow(new IllegalStateException(mensajeError)).when(favoritoService).agregarFavorito(anyString(), anyString());
        mockMvc.perform(post(ViewNames.FAVORITOS_URL + "/agregar")
                        .param("medicamentoId", "med1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ViewNames.CATALOGO_URL))
                .andExpect(flash().attribute("errorMessage", "Error al añadir a favoritos: " + mensajeError));
    }

    /**
     * Verifica que al eliminar un favorito desde el catalogo, se redirige
     * de vuelta al catalogo.
     * @throws Exception si ocurre un error durante la peticion.
     */
    @Test
    @WithMockUser(username = "testuser")
    void cuandoEliminaFavorito_desdeCatalogo_debeRedirigirACatalogo() throws Exception {
        doNothing().when(favoritoService).eliminarFavorito("testuser", "med1");
        mockMvc.perform(post(ViewNames.FAVORITOS_URL + "/eliminar")
                        .param("medicamentoId", "med1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ViewNames.CATALOGO_URL));
        verify(favoritoService).eliminarFavorito("testuser", "med1");
    }

    /**
     * Verifica que al eliminar un favorito desde la pagina de favoritos,
     * se redirige de vuelta a la pagina de favoritos.
     * @throws Exception si ocurre un error durante la peticion.
     */
    @Test
    @WithMockUser(username = "testuser")
    void cuandoEliminaFavorito_desdeFavoritos_debeRedirigirAFavoritos() throws Exception {
        doNothing().when(favoritoService).eliminarFavorito("testuser", "med1");
        mockMvc.perform(post(ViewNames.FAVORITOS_URL + "/eliminar")
                        .param("medicamentoId", "med1")
                        .param("source", "favoritos")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ViewNames.FAVORITOS_URL));
        verify(favoritoService).eliminarFavorito("testuser", "med1");
    }
}