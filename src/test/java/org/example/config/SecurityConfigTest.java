package org.example.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Configuration
    @EnableWebSecurity
    static class TestSecurityConfig {

        @Bean
        public UserDetailsService userDetailsService() {
            InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
            manager.createUser(User.withUsername("user").password("{noop}password").roles("USER").build());
            manager.createUser(User.withUsername("admin").password("{noop}adminpass").roles("ADMIN").build());
            return manager;
        }
    }

    @Autowired
    private ApplicationContext context;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextoCarga() {
        assertThat(context).isNotNull();
        // assertThat(passwordEncoder).isNotNull();
        assertThat(authenticationManager).isNotNull();
        assertThat(securityFilterChain).isNotNull();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void codificadorPasswordEsBCrypt() {
        assertThat(passwordEncoder).isInstanceOf(org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.class);
    }

    @Test
    public void urlsPublicasSonAccesibles() throws Exception {
        String[] publicUrls = {
                "/",
                "/css/style.css",
                "/registro",
                "/login",
                "/logout",
                "/api/test",
                "/medicamentos/catalogo"
        };

        for (String url : publicUrls) {
            mockMvc.perform(get(url))
                    .andExpect(status().isOk());
        }
    }

    @Test
    public void urlsProtegidasRequierenAutenticacion() throws Exception {
        mockMvc.perform(get("/medicamentos/privado"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    public void paginaLoginEsAccesible() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login")); // Ajustado a vista real
    }

    @Test
    public void loginConUsuarioValido() throws Exception {
        mockMvc.perform(formLogin().user("user").password("password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medicamentos/catalogo"));
    }

    @Test
    public void logoutRedirigeALogin() throws Exception {
        mockMvc.perform(logout())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout=true"));
    }

    @Test
    @WithMockUser
    public void usuarioAutenticadoPuedeAccederUrlProtegida() throws Exception {
        mockMvc.perform(get("/medicamentos/catalogo"))
                .andExpect(status().isOk());
    }
}
