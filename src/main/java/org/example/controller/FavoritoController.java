package org.example.controller;

import org.example.config.ViewNames;
import org.example.model.Favorito;
import org.example.model.Medicamento;
import org.example.service.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @GetMapping(ViewNames.FAVORITOS_URL)
    public String verMisFavoritos(Model model, Principal principal) {
        if (principal == null) {
            return ViewNames.REDIRECT_LOGIN;
        }
        String username = principal.getName();
        List<Medicamento> medicamentosFavoritos = favoritoService.obtenerFavoritosPorUsuario(username)
                .stream()
                .map(Favorito::getMedicamento)
                .collect(Collectors.toList());
        model.addAttribute("medicamentos", medicamentosFavoritos);
        return ViewNames.FAVORITOS_VIEW;
    }

    @PostMapping(ViewNames.FAVORITOS_URL + "/agregar")
    public String agregarFavorito(@RequestParam("medicamentoId") String medicamentoId,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return ViewNames.REDIRECT_LOGIN;
        }
        try {
            String username = principal.getName();
            favoritoService.agregarFavorito(username, medicamentoId);
            redirectAttributes.addFlashAttribute("successMessage", "Medicamento añadido a favoritos.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al añadir a favoritos: " + e.getMessage());
        }
        return ViewNames.REDIRECT_CATALOGO;
    }

    @PostMapping(ViewNames.FAVORITOS_URL + "/eliminar")
    public String eliminarFavorito(@RequestParam("medicamentoId") String medicamentoId,
                                   @RequestParam(name = "source", required = false) String source,
                                   Principal principal,
                                   RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return ViewNames.REDIRECT_LOGIN;
        }
        try {
            String username = principal.getName();
            favoritoService.eliminarFavorito(username, medicamentoId);
            redirectAttributes.addFlashAttribute("successMessage", "Medicamento eliminado de favoritos.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar de favoritos: " + e.getMessage());
        }
        if ("favoritos".equals(source)) {
            return ViewNames.REDIRECT_FAVORITOS;
        }
        return ViewNames.REDIRECT_CATALOGO;
    }
}