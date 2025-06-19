package org.example.controller;

import org.example.config.SecurityConfig;
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

    // --- Tests para GET /login ---

    @Test
    void cuandoPideLoginPage_debeMostrarVistaLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    void cuandoPideLoginPage_conParametroError_debeAñadirErrorMessageAlModelo() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    // --- Tests para GET /registro ---

    @Test
    void cuandoPideRegistroPage_debeMostrarVistaRegistro() throws Exception {
        mockMvc.perform(get("/registro"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registro"))
                .andExpect(model().attributeExists("usuario")); // Verifica que se añade un objeto Usuario vacío al modelo
    }

    // --- Tests para POST /registro ---

    @Test
    void cuandoRegistraUsuario_conDatosValidos_debeRedirigirALoginConMensajeExito() throws Exception {
        // Arrange
        // Simulamos que el servicio de usuario guarda el objeto sin problemas y lo devuelve
        when(usuarioService.registrarUsuario(any(Usuario.class))).thenReturn(new Usuario());

        // Act & Assert
        mockMvc.perform(post("/registro")
                        .param("username", "nuevo_usuario")
                        .param("email", "nuevo@email.com")
                        .param("password", "password123")
                        .with(csrf())) // No olvides el token CSRF para peticiones POST
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("successMessage", "¡Registro exitoso! Por favor, inicia sesión."));
    }

    @Test
    void cuandoRegistraUsuario_conDatosInvalidos_debeVolverARegistroConErrores() throws Exception {
        // Act & Assert
        // Enviamos un username demasiado corto para que falle la validación @Size(min=3)
        mockMvc.perform(post("/registro")
                        .param("username", "a")
                        .param("email", "invalido") // Email inválido
                        .param("password", "") // Contraseña vacía
                        .with(csrf()))
                .andExpect(status().isOk()) // Esperamos OK (200) porque no redirige, vuelve a mostrar la misma página
                .andExpect(view().name("auth/registro"))
                .andExpect(model().hasErrors()); // Verificamos que el modelo contiene errores de validación
    }

    @Test
    void cuandoRegistraUsuario_yServicioLanzaExcepcion_debeVolverARegistroConMensajeError() throws Exception {
        // Arrange
        String mensajeError = "El email ya está registrado";
        // Simulamos que el servicio lanza una excepción (ej. porque el email ya existe)
        doThrow(new Exception(mensajeError)).when(usuarioService).registrarUsuario(any(Usuario.class));

        // Act & Assert
        mockMvc.perform(post("/registro")
                        .param("username", "usuario_valido")
                        .param("email", "repetido@email.com")
                        .param("password", "password123")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registro"))
                .andExpect(model().attribute("errorMessage", mensajeError));
    }
}