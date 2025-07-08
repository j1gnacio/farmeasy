package org.example.config;

/**
 * Clase final que contiene constantes para los nombres de las vistas y las rutas de URL.
 * Esto ayuda a evitar errores de tipeo y a mantener la consistencia en todo el proyecto.
 */
public final class ViewNames {

    private ViewNames() {} // Constructor privado para que no se pueda instanciar

    // NOMBRES DE VISTAS (para return en controladores)
    public static final String LOGIN_VIEW = "auth/login";
    public static final String REGISTRO_VIEW = "auth/registro";
    public static final String CATALOGO_VIEW = "medicamentos/catalogo";
    public static final String DETALLE_MEDICAMENTO_VIEW = "medicamentos/detalle";
    public static final String FAVORITOS_VIEW = "favoritos/lista";
    public static final String HISTORIAL_VIEW = "historial/lista";
    public static final String INDEX_VIEW = "index";

    // PATRONES DE URL (para @GetMapping y SecurityConfig)
    public static final String ROOT_URL = "/";
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";
    public static final String REGISTRO_URL = "/registro";
    public static final String MEDICAMENTOS_URL = "/medicamentos";
    public static final String CATALOGO_URL = "/medicamentos/catalogo";
    public static final String FAVORITOS_URL = "/favoritos";
    public static final String HISTORIAL_URL = "/historial";

    // RUTAS DE REDIRECCIÓN (para return "redirect:...")
    public static final String REDIRECT_LOGIN = "redirect:" + LOGIN_URL;
    public static final String REDIRECT_CATALOGO = "redirect:" + CATALOGO_URL;
    public static final String REDIRECT_FAVORITOS = "redirect:" + FAVORITOS_URL;
}