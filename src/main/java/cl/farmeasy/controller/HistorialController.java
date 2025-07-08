package cl.farmeasy.controller;

import cl.farmeasy.config.ViewNames;
import cl.farmeasy.model.HistorialBusqueda;
import cl.farmeasy.service.HistorialBusquedaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

/**
 * Controlador para gestionar el historial de búsqueda de los usuarios.
 */
@Controller
public class HistorialController {

    @Autowired
    private HistorialBusquedaService historialService;

    /**
     * Muestra el historial de búsqueda del usuario autenticado.
     *
     * @param model El modelo para pasar datos a la vista.
     * @param principal El objeto Principal que representa al usuario autenticado.
     * @return La vista del historial de búsqueda.
     */
    @GetMapping(ViewNames.HISTORIAL_URL)
    public String verHistorial(Model model, Principal principal) {
        if (principal == null) {
            return ViewNames.REDIRECT_LOGIN;
        }
        String username = principal.getName();
        List<HistorialBusqueda> historial = historialService.obtenerHistorialPorUsuario(username);
        model.addAttribute("historial", historial);
        return ViewNames.HISTORIAL_VIEW;
    }
}