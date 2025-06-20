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
                        .requestMatchers(
                                ViewNames.ROOT_URL,
                                "/css/**", "/js/**", "/img/**", "/api/**",
                                ViewNames.REGISTRO_URL,
                                ViewNames.LOGIN_URL,
                                ViewNames.LOGOUT_URL,
                                ViewNames.MEDICAMENTOS_URL + "/**" // Permite /catalogo y /detalle
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage(ViewNames.LOGIN_URL)
                        .loginProcessingUrl(ViewNames.LOGIN_URL)
                        .defaultSuccessUrl(ViewNames.CATALOGO_URL, true)
                        .failureUrl(ViewNames.LOGIN_URL + "?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl(ViewNames.LOGOUT_URL)
                        .logoutSuccessUrl(ViewNames.LOGIN_URL + "?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .userDetailsService(userDetailsService);

        return http.build();
    }
}