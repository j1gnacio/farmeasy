package org.example.controller;

import org.example.config.SecurityConfig;
import org.example.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(HomeController.class) // Enfocamos el test solo en HomeController
@Import(SecurityConfig.class) // Importamos la configuración de seguridad
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Aunque HomeController no usa UserDetailsService, lo necesitamos
    // porque importamos SecurityConfig, que sí lo requiere.
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void cuandoPideHomePage_debeDevolverVistaIndex() throws Exception {
        // Act & Assert
        // Hacemos una petición GET a la raíz "/"
        mockMvc.perform(get("/"))
                .andExpect(status().isOk()) // Esperamos que la respuesta sea 200 OK
                .andExpect(view().name("index")); // Esperamos que se renderice la vista "index"
    }
}