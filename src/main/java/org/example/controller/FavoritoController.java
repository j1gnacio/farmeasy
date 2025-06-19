package org.example.controller;

import org.example.model.Favorito; // Asegúrate que esta importación esté si usas Favorito directamente
import org.example.model.Medicamento;
import org.example.service.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // ¡MUY IMPORTANTE!
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping; // ¡MUY IMPORTANTE!
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller // <-- ¿Está esta anotación?
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @GetMapping("/favoritos") // <-- ¿Está esta anotación con la ruta correcta?
    public String verMisFavoritos(Model model, Principal principal) {
        if (principal == null) {
            // Si por alguna razón el usuario no está logueado, lo mandamos al login
            return "redirect:/login";
        }

        String username = principal.getName();

        // Obtenemos la lista completa de favoritos y de ahí extraemos los medicamentos
        List<Medicamento> medicamentosFavoritos = favoritoService.obtenerFavoritosPorUsuario(username)
                .stream()
                .map(Favorito::getMedicamento)
                .collect(Collectors.toList());

        // Pasamos la lista de medicamentos a la vista
        model.addAttribute("medicamentos", medicamentosFavoritos);

        // Le decimos a Spring que renderice la plantilla que está en "templates/favoritos/lista.html"
        return "favoritos/lista";
    }
    // =================================================================


    @PostMapping("/favoritos/agregar")
    public String agregarFavorito(@RequestParam("medicamentoId") String medicamentoId,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {
        // ... (código existente, sin cambios)
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            String username = principal.getName();
            favoritoService.agregarFavorito(username, medicamentoId);
            redirectAttributes.addFlashAttribute("successMessage", "Medicamento añadido a favoritos.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al añadir a favoritos: " + e.getMessage());
        }

        return "redirect:/medicamentos/catalogo";
    }

    @PostMapping("/favoritos/eliminar")
    public String eliminarFavorito(@RequestParam("medicamentoId") String medicamentoId,
                                   @RequestParam(name = "source", required = false) String source,
                                   Principal principal,
                                   RedirectAttributes redirectAttributes) {
        // ... (código existente, sin cambios)
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            String username = principal.getName();
            favoritoService.eliminarFavorito(username, medicamentoId);
            redirectAttributes.addFlashAttribute("successMessage", "Medicamento eliminado de favoritos.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar de favoritos: " + e.getMessage());
        }

        if ("favoritos".equals(source)) {
            return "redirect:/favoritos";
        }

        return "redirect:/medicamentos/catalogo";
    }
}