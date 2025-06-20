package org.example.controller;

import org.example.config.ViewNames;
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