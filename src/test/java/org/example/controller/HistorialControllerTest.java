package org.example.controller;

import org.example.config.SecurityConfig;
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

@WebMvcTest(HistorialController.class) // Enfocamos el test solo en HistorialController
@Import(SecurityConfig.class) // Importamos la configuración de seguridad para que maneje las redirecciones correctamente
class HistorialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistorialBusquedaService historialService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService; // Necesario para el contexto de seguridad en tests

    // --- Test para GET /historial ---

    @Test
    @WithMockUser(username = "testuser") // Simula que un usuario llamado "testuser" ha iniciado sesión
    void cuandoUsuarioLogueadoPideHistorial_debeMostrarVistaDeLista() throws Exception {
        // Arrange
        // Creamos una lista falsa de historial que nuestro servicio mock devolverá
        HistorialBusqueda busqueda = new HistorialBusqueda("Paracetamol", null); // El usuario no es necesario para este test de controlador
        when(historialService.obtenerHistorialPorUsuario("testuser")).thenReturn(List.of(busqueda));

        // Act & Assert
        mockMvc.perform(get("/historial"))
                .andExpect(status().isOk()) // Esperamos un código 200 OK
                .andExpect(view().name("historial/lista")) // Esperamos que se renderice la vista correcta
                .andExpect(model().attributeExists("historial")); // Verificamos que el modelo contiene el atributo "historial"

        // Verificamos que el método del servicio fue llamado con el nombre de usuario correcto
        verify(historialService).obtenerHistorialPorUsuario("testuser");
    }

    @Test
    void cuandoUsuarioNoLogueadoPideHistorial_debeRedirigirALogin() throws Exception {
        // Act & Assert
        // Hacemos la petición sin @WithMockUser, simulando un usuario anónimo
        mockMvc.perform(get("/historial"))
                .andExpect(status().is3xxRedirection()) // Esperamos una redirección (código 302)
                .andExpect(redirectedUrlPattern("**/login")); // Verificamos que la redirección es a la página de login
    }
}