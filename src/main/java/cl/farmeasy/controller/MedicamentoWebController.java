package cl.farmeasy.controller;

import cl.farmeasy.service.HistorialBusquedaService;
import cl.farmeasy.config.ViewNames;
import cl.farmeasy.model.Medicamento;
import cl.farmeasy.service.FavoritoService;
import cl.farmeasy.service.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Controlador web para gestionar las interacciones de los usuarios con los medicamentos.
 * Maneja la visualización del catálogo y los detalles de los medicamentos.
 */
@Controller
@RequestMapping(ViewNames.MEDICAMENTOS_URL)
public class MedicamentoWebController {

    @Autowired
    private MedicamentoService medicamentoService;
    @Autowired
    private FavoritoService favoritoService;
    @Autowired
    private HistorialBusquedaService historialBusquedaService;

    /**
     * Muestra el catálogo de medicamentos.
     *
     * @param model El modelo para pasar datos a la vista.
     * @param busqueda El término de búsqueda opcional.
     * @return La vista del catálogo de medicamentos.
     */
    @GetMapping("/catalogo")
    public String mostrarCatalogo(Model model,
                                  @RequestParam(required = false) String busqueda) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isUserAuthenticated = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName());

        if (busqueda != null && !busqueda.trim().isEmpty() && isUserAuthenticated) {
            historialBusquedaService.guardarBusqueda(busqueda, auth.getName());
        }

        List<Medicamento> medicamentos;
        if (busqueda != null && !busqueda.isEmpty()) {
            medicamentos = medicamentoService.buscarPorNombre(busqueda);
            model.addAttribute("busquedaActual", busqueda);
        } else {
            medicamentos = medicamentoService.obtenerTodos();
        }
        model.addAttribute("medicamentos", medicamentos);

        if (isUserAuthenticated) {
            Set<String> favoritosIds = favoritoService.obtenerIdsMedicamentosFavoritos(auth.getName());
            model.addAttribute("favoritosIds", favoritosIds);
        } else {
            model.addAttribute("favoritosIds", Collections.emptySet());
        }
        return ViewNames.CATALOGO_VIEW;
    }

    /**
     * Muestra la página de detalles de un medicamento.
     *
     * @param id El ID del medicamento a mostrar.
     * @param model El modelo para pasar datos a la vista.
     * @return La vista de detalle del medicamento.
     */
    @GetMapping("/{id}")
    public String verDetalleMedicamento(@PathVariable("id") String id, Model model) {
        Medicamento medicamentoPrincipal = medicamentoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado con ID: " + id));
        model.addAttribute("medicamento", medicamentoPrincipal);

        List<Medicamento> comparativa = medicamentoService.buscarPorNombre(medicamentoPrincipal.getNombre());
        model.addAttribute("comparativaPrecios", comparativa);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            boolean esFavorito = favoritoService.esFavorito(auth.getName(), id);
            model.addAttribute("esFavorito", esFavorito);
        }
        return ViewNames.DETALLE_MEDICAMENTO_VIEW;
    }
}