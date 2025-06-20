package org.example.controller;

import org.example.config.SecurityConfig;
import org.example.config.ViewNames; // <-- Importamos las constantes
import org.example.exception.RegistroException;
import org.example.model.Usuario;
import org.example.security.UserDetailsServiceImpl;
import org.example.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UsuarioService usuarioService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void cuandoPideLoginPage_debeMostrarVistaLogin() throws Exception {
        mockMvc.perform(get(ViewNames.LOGIN_URL)) // <-- CORREGIDO
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.LOGIN_VIEW)); // <-- CORREGIDO
    }

    @Test
    void cuandoPideLoginPage_conParametroError_debeAñadirErrorMessageAlModelo() throws Exception {
        mockMvc.perform(get(ViewNames.LOGIN_URL).param("error", "true")) // <-- CORREGIDO
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.LOGIN_VIEW)) // <-- CORREGIDO
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    void cuandoPideRegistroPage_debeMostrarVistaRegistro() throws Exception {
        mockMvc.perform(get(ViewNames.REGISTRO_URL)) // <-- CORREGIDO
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.REGISTRO_VIEW)) // <-- CORREGIDO
                .andExpect(model().attributeExists("usuario"));
    }

    @Test
    void cuandoRegistraUsuario_conDatosValidos_debeRedirigirALoginConMensajeExito() throws Exception {
        when(usuarioService.registrarUsuario(any(Usuario.class))).thenReturn(new Usuario());

        mockMvc.perform(post(ViewNames.REGISTRO_URL) // <-- CORREGIDO
                        .param("username", "nuevo_usuario")
                        .param("email", "nuevo@email.com")
                        .param("password", "password123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ViewNames.LOGIN_URL)) // <-- CORREGIDO
                .andExpect(flash().attribute("successMessage", "¡Registro exitoso! Por favor, inicia sesión."));
    }

    @Test
    void cuandoRegistraUsuario_conDatosInvalidos_debeVolverARegistroConErrores() throws Exception {
        mockMvc.perform(post(ViewNames.REGISTRO_URL) // <-- CORREGIDO
                        .param("username", "a")
                        .param("email", "invalido")
                        .param("password", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.REGISTRO_VIEW)) // <-- CORREGIDO
                .andExpect(model().hasErrors());
    }

    @Test
    void cuandoRegistraUsuario_yServicioLanzaExcepcion_debeVolverARegistroConMensajeError() throws Exception {
        // Arrange
        String mensajeError = "El email ya está registrado";

        doThrow(new RegistroException(mensajeError)).when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act & Assert (el resto del test se mantiene igual)
        mockMvc.perform(post(ViewNames.REGISTRO_URL)
                        .param("username", "usuario_valido")
                        .param("email", "repetido@email.com")
                        .param("password", "password123")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.REGISTRO_VIEW))
                .andExpect(model().attribute("errorMessage", mensajeError));
    }
}