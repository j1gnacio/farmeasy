package org.example.controller;

import org.example.model.HistorialBusqueda;
import org.example.service.HistorialBusquedaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HistorialController {

    @Autowired
    private HistorialBusquedaService historialService;

    @GetMapping("/historial") // <-- ¡Esta es la anotación clave que soluciona el error!
    public String verHistorial(Model model, Principal principal) {
        // 1. Verifica si el usuario está logueado
        if (principal == null) {
            return "redirect:/login";
        }

        // 2. Obtiene el historial del usuario logueado
        String username = principal.getName();
        List<HistorialBusqueda> historial = historialService.obtenerHistorialPorUsuario(username);

        // 3. Pasa la lista del historial a la vista
        model.addAttribute("historial", historial);

        return "historial/lista";
    }
}