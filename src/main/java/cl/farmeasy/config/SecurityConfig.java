package cl.farmeasy.config;

import cl.farmeasy.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion de seguridad para la aplicacion.
 * Define las reglas de acceso a las URL, el formulario de inicio de sesion y el codificador de contrasenas.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Constructor para la inyeccion de dependencias de la configuracion de seguridad.
     *
     * @param userDetailsService El servicio para cargar los detalles del usuario.
     */
    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Define el bean para el codificador de contrasenas, utilizando BCrypt.
     *
     * @return Una instancia de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define el bean para el AuthenticationManager.
     *
     * @param authenticationConfiguration La configuracion de autenticacion.
     * @return El AuthenticationManager.
     * @throws Exception si ocurre un error al obtener el AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configura la cadena de filtros de seguridad.
     *
     * @param http El objeto HttpSecurity para configurar la seguridad.
     * @return La cadena de filtros de seguridad.
     * @throws Exception si ocurre un error durante la configuracion.
     */
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
                                ViewNames.MEDICAMENTOS_URL + "/**"
                        ).permitAll()
                        .requestMatchers("/perfil/**").authenticated()
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