package org.example.controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.example.model.Medicamento;
import org.example.service.FavoritoService;
import org.example.service.HistorialBusquedaService; // <-- 1. Importación necesaria
import org.example.service.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/medicamentos")
public class MedicamentoWebController {

    @Autowired
    private MedicamentoService medicamentoService;

    @Autowired
    private FavoritoService favoritoService;

    // 2. Inyectamos el nuevo servicio para poder usarlo.
    @Autowired
    private HistorialBusquedaService historialBusquedaService;

    @GetMapping("/catalogo")
    public String mostrarCatalogo(Model model,
                                  @RequestParam(required = false) String busqueda) {

        // --- 3. LÓGICA PARA GUARDAR EL HISTORIAL DE BÚSQUEDA ---
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Solo guardamos si hay un término de búsqueda y si el usuario está logueado.
        if (busqueda != null && !busqueda.trim().isEmpty() &&
                auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {

            String username = auth.getName();
            // Llamamos al servicio para que guarde el registro.
            historialBusquedaService.guardarBusqueda(busqueda, username);
        }

        // --- Lógica de búsqueda de medicamentos (sin cambios) ---
        List<Medicamento> medicamentos;
        if (busqueda != null && !busqueda.isEmpty()) {
            medicamentos = medicamentoService.buscarPorNombre(busqueda);
            model.addAttribute("busquedaActual", busqueda);
        } else {
            medicamentos = medicamentoService.obtenerTodos();
        }
        model.addAttribute("medicamentos", medicamentos);

        // --- Lógica de favoritos (sin cambios) ---
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            String username = auth.getName();
            Set<String> favoritosIds = favoritoService.obtenerIdsMedicamentosFavoritos(username);
            model.addAttribute("favoritosIds", favoritosIds);
        } else {
            model.addAttribute("favoritosIds", Collections.emptySet());
        }

        return "medicamentos/catalogo";
    }

    // --- Método para la página de detalle (sin cambios) ---
    @GetMapping("/{id}")
    public String verDetalleMedicamento(@PathVariable("id") String id, Model model) {

        Medicamento medicamentoPrincipal = medicamentoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado con ID: " + id));
        model.addAttribute("medicamento", medicamentoPrincipal);

        List<Medicamento> comparativa = medicamentoService.buscarPorNombre(medicamentoPrincipal.getNombre());
        model.addAttribute("comparativaPrecios", comparativa);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            String username = auth.getName();
            boolean esFavorito = favoritoService.esFavorito(username, id);
            model.addAttribute("esFavorito", esFavorito);
        }

        return "medicamentos/detalle";
    }
}