package org.example.config;

import org.example.security.UserDetailsServiceImpl;
import org.example.service.FavoritoService;
import org.example.service.HistorialBusquedaService;
import org.example.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integracion para la configuracion de seguridad (SecurityConfig).
 * Verifica las reglas de acceso a URL, y los flujos de login y logout.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    // Mockeamos todos los servicios para aislar la prueba a la capa web y de seguridad.
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private UsuarioService usuarioService;
    @MockBean
    private FavoritoService favoritoService;
    @MockBean
    private HistorialBusquedaService historialBusquedaService;

    /**
     * Verifica que las URLs publicas son accesibles para usuarios no autenticados.
     * @throws Exception si ocurre un error durante la peticion.
     */
    @Test
    @WithAnonymousUser // Simula un usuario no autenticado
    public void cuandoUsuarioNoAutenticado_permiteAccesoAUrlsPublicas() throws Exception {
        mockMvc.perform(get(ViewNames.LOGIN_URL)).andExpect(status().isOk());
        mockMvc.perform(get(ViewNames.REGISTRO_URL)).andExpect(status().isOk());
        mockMvc.perform(get(ViewNames.CATALOGO_URL)).andExpect(status().isOk());
        mockMvc.perform(get("/css/style.css")).andExpect(status().isOk());
    }

    /**
     * Verifica que las URLs protegidas redirigen al login si el usuario no esta autenticado.
     * @throws Exception si ocurre un error durante la peticion.
     */
    @Test
    @WithAnonymousUser
    public void cuandoUsuarioNoAutenticado_redirigeUrlsProtegidasALogin() throws Exception {
        mockMvc.perform(get(ViewNames.FAVORITOS_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        mockMvc.perform(get(ViewNames.HISTORIAL_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    /**
     * Verifica que un usuario autenticado puede acceder a las URLs protegidas.
     * @throws Exception si ocurre un error durante la peticion.
     */
    @Test
    @WithMockUser // Simula un usuario logueado con el rol "USER" por defecto
    public void cuandoUsuarioAutenticado_permiteAccesoAUrlsProtegidas() throws Exception {
        mockMvc.perform(get(ViewNames.FAVORITOS_URL))
                .andExpect(status().isOk());

        mockMvc.perform(get(ViewNames.HISTORIAL_URL))
                .andExpect(status().isOk());
    }

    /**
     * Simula un intento de inicio de sesion con credenciales incorrectas.
     * Espera una redireccion a la pagina de login con un parametro de error.
     * @throws Exception si ocurre un error durante la peticion.
     */
    @Test
    public void cuandoLoginFalla_debeRedirigirALoginConError() throws Exception {
        mockMvc.perform(formLogin(ViewNames.LOGIN_URL).user("user").password("wrongpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ViewNames.LOGIN_URL + "?error=true"));
    }

    /**
     * Simula el proceso de cierre de sesion.
     * Espera una redireccion a la pagina de login con un parametro de logout.
     * @throws Exception si ocurre un error durante la peticion.
     */
    @Test
    @WithMockUser
    public void cuandoLogout_debeRedirigirALoginConMensaje() throws Exception {
        mockMvc.perform(logout(ViewNames.LOGOUT_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ViewNames.LOGIN_URL + "?logout=true"));
    }
}