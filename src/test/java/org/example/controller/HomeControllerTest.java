package org.example.controller;

import org.example.config.SecurityConfig;
import org.example.config.ViewNames; // <-- Importamos las constantes
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

@WebMvcTest(HomeController.class)
@Import(SecurityConfig.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void cuandoPideHomePage_debeDevolverVistaIndex() throws Exception {
        mockMvc.perform(get(ViewNames.ROOT_URL)) // <-- CORREGIDO
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.INDEX_VIEW)); // <-- CORREGIDO
    }
}