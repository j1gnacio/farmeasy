package cl.farmeasy.controller;

import cl.farmeasy.config.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la página de inicio de la aplicación.
 */
@Controller
public class HomeController {

    /**
     * Muestra la página de inicio.
     *
     * @return La vista de la página de inicio.
     */
    @GetMapping(ViewNames.ROOT_URL)
    public String home() {
        return ViewNames.INDEX_VIEW;
    }
}