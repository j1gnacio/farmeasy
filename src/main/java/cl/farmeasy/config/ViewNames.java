package cl.farmeasy.config;

/**
 * Clase final que contiene constantes para los nombres de las vistas y las rutas de URL.
 * Esto ayuda a evitar errores de tipeo y a mantener la consistencia en todo el proyecto.
 */
public final class ViewNames {

    private ViewNames() {}

    // ================== NOMBRES DE VISTAS (para return en controladores) ==================
    /** Vista para la pagina de inicio de sesion. */
    public static final String LOGIN_VIEW = "auth/login";
    /** Vista para la pagina de registro. */
    public static final String REGISTRO_VIEW = "auth/registro";
    /** Vista para el catalogo de medicamentos. */
    public static final String CATALOGO_VIEW = "medicamentos/catalogo";
    /** Vista para el detalle de un medicamento. */
    public static final String DETALLE_MEDICAMENTO_VIEW = "medicamentos/detalle";
    /** Vista para la lista de favoritos. */
    public static final String FAVORITOS_VIEW = "favoritos/lista";
    /** Vista para el historial de busqueda. */
    public static final String HISTORIAL_VIEW = "historial/lista";
    /** Vista para la pagina de inicio. */
    public static final String INDEX_VIEW = "index";

    // ================== PATRONES DE URL (para @GetMapping y SecurityConfig) ==================
    /** URL para la raiz del sitio. */
    public static final String ROOT_URL = "/";
    /** URL para la pagina de inicio de sesion. */
    public static final String LOGIN_URL = "/login";
    /** URL para cerrar sesion. */
    public static final String LOGOUT_URL = "/logout";
    /** URL para la pagina de registro. */
    public static final String REGISTRO_URL = "/registro";
    /** URL base para los medicamentos. */
    public static final String MEDICAMENTOS_URL = "/medicamentos";
    /** URL para el catalogo de medicamentos. */
    public static final String CATALOGO_URL = "/medicamentos/catalogo";
    /** URL para la gestion de favoritos. */
    public static final String FAVORITOS_URL = "/favoritos";
    /** URL para el historial de busqueda. */
    public static final String HISTORIAL_URL = "/historial";

    // ================== RUTAS DE REDIRECCION (para return "redirect:...") ==================
    /** Redireccion a la pagina de inicio de sesion. */
    public static final String REDIRECT_LOGIN = "redirect:" + LOGIN_URL;
    /** Redireccion al catalogo de medicamentos. */
    public static final String REDIRECT_CATALOGO = "redirect:" + CATALOGO_URL;
    /** Redireccion a la pagina de favoritos. */
    public static final String REDIRECT_FAVORITOS = "redirect:" + FAVORITOS_URL;
}