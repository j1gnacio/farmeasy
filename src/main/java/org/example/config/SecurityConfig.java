package org.example.config;

import org.example.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/css/**", "/js/**", "/img/**", "/registro", "/login", "/logout", "/api/**").permitAll() // Permitir acceso a API, y recursos estáticos
                        .requestMatchers("/medicamentos/**").permitAll() // Catálogo público
                        // .requestMatchers("/admin/**").hasRole("ADMIN") // Ejemplo para rutas de admin
                        .anyRequest().authenticated() // Todas las demás rutas requieren autenticación
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login") // Página de login personalizada
                        .loginProcessingUrl("/login") // URL donde Spring Security procesa el login
                        .defaultSuccessUrl("/medicamentos/catalogo", true) // Redirigir aquí después del login exitoso
                        .failureUrl("/login?error=true") // Redirigir aquí en caso de error de login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true") // Redirigir aquí después del logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .userDetailsService(userDetailsService);
        // .csrf(csrf -> csrf.disable()); // Deshabilitar CSRF si usas APIs sin protección CSRF (no recomendado para prod si tienes forms)

        return http.build();
    }
}