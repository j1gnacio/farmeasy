package org.example.controller;

import org.example.model.Usuario;
import org.example.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    /**
     * Prueba funcional para la página de login.
     * Verifica que la página se cargue correctamente y que los mensajes de error y logout se muestren.
     * Se ajusta para no esperar ModelAndView, sino verificar contenido y status.
     */
    @Test
    @WithMockUser
    public void testLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Please sign in")));

        mockMvc.perform(MockMvcRequestBuilders.get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Usuario o contraseña incorrectos")));

        mockMvc.perform(MockMvcRequestBuilders.get("/login").param("logout", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Has cerrado sesión exitosamente")));
    }

    /**
     * Prueba funcional para la página de registro.
     * Verifica que el modelo contenga un objeto Usuario para el formulario.
     */
    @Test
    @WithMockUser
    public void testRegistroPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/registro"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registro"))
                .andExpect(model().attributeExists("usuario"));
    }

    /**
     * Prueba funcional para registrar un usuario con datos válidos.
     * Verifica que la redirección sea a la página de login con mensaje de éxito.
     */
    @Test
    @WithMockUser
    public void testRegistrarUsuario_Valid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/registro")
                        .param("username", "usuario1")
                        .param("password", "password123")
                        .param("email", "usuario1@example.com")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    /**
     * Prueba funcional para registrar un usuario con datos inválidos.
     * Verifica que se regrese al formulario con errores.
     */
    @Test
    @WithMockUser
    public void testRegistrarUsuario_Invalid() throws Exception {
        // Simular error de validación enviando datos vacíos
        mockMvc.perform(MockMvcRequestBuilders.post("/registro")
                        .param("username", "")
                        .param("password", "")
                        .param("email", "")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registro"));
    }
    /**
     * Prueba funcional para manejar excepción durante el registro.
     * Verifica que se muestre el mensaje de error en el modelo.
     */
    @Test
    @WithMockUser
    public void testRegistrarUsuario_Exception() throws Exception {
        doThrow(new RuntimeException("Error al registrar usuario")).when(usuarioService).registrarUsuario(any(Usuario.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/registro")
                        .param("username", "usuario2")
                        .param("password", "password123")
                        .param("email", "usuario2@example.com")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registro"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    /**
     * Prueba funcional para validar que el nombre de usuario es obligatorio.
     */
    @Test
    @WithMockUser
    public void testRegistrarUsuario_UsernameRequired() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/registro")
                        .param("username", "")
                        .param("password", "password123")
                        .param("email", "usuario@example.com")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registro"))
                .andExpect(model().attributeHasFieldErrors("usuario", "username"));
    }

    /**
     * Prueba funcional para validar que el email es obligatorio y con formato correcto.
     */
    @Test
    @WithMockUser
    public void testRegistrarUsuario_EmailRequiredAndFormat() throws Exception {
        // Email vacío
        mockMvc.perform(MockMvcRequestBuilders.post("/registro")
                        .param("username", "usuario")
                        .param("password", "password123")
                        .param("email", "")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registro"))
                .andExpect(model().attributeHasFieldErrors("usuario", "email"));

        // Email con formato inválido
        mockMvc.perform(MockMvcRequestBuilders.post("/registro")
                        .param("username", "usuario")
                        .param("password", "password123")
                        .param("email", "emailinvalido")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registro"))
                .andExpect(model().attributeHasFieldErrors("usuario", "email"));
    }

    /**
     * Prueba funcional para validar que la contraseña es obligatoria y con longitud mínima.
     */
    @Test
    @WithMockUser
    public void testRegistrarUsuario_PasswordRequiredAndMinLength() throws Exception {
        // Contraseña vacía
        mockMvc.perform(MockMvcRequestBuilders.post("/registro")
                        .param("username", "usuario")
                        .param("password", "")
                        .param("email", "usuario@example.com")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registro"))
                .andExpect(model().attributeHasFieldErrors("usuario", "password"));

        // Contraseña muy corta
        mockMvc.perform(MockMvcRequestBuilders.post("/registro")
                        .param("username", "usuario")
                        .param("password", "123")
                        .param("email", "usuario@example.com")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registro"))
                .andExpect(model().attributeHasFieldErrors("usuario", "password"));
    }
}
