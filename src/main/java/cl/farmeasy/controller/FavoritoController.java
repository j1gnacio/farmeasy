package cl.farmeasy.controller;

import cl.farmeasy.config.ViewNames;
import cl.farmeasy.model.Favorito;
import cl.farmeasy.model.Medicamento;
import cl.farmeasy.service.FavoritoService;
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

/**
 * Controlador para gestionar las acciones relacionadas con los medicamentos favoritos de un usuario.
 */
@Controller
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    /**
     * Muestra la lista de medicamentos favoritos del usuario autenticado.
     *
     * @param model El modelo para pasar datos a la vista.
     * @param principal El objeto Principal que representa al usuario autenticado.
     * @return La vista de la lista de favoritos.
     */
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

    /**
     * Agrega un medicamento a la lista de favoritos del usuario.
     *
     * @param medicamentoId El ID del medicamento a agregar.
     * @param principal El usuario autenticado.
     * @param redirectAttributes Atributos para mensajes flash.
     * @return Redirige al catálogo de medicamentos.
     */
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

    /**
     * Elimina un medicamento de la lista de favoritos del usuario.
     *
     * @param medicamentoId El ID del medicamento a eliminar.
     * @param source La página de origen de la solicitud para una redirección inteligente.
     * @param principal El usuario autenticado.
     * @param redirectAttributes Atributos para mensajes flash.
     * @return Redirige a la página de origen (catálogo o favoritos).
     */
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