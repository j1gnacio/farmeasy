package org.example.controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.example.model.Medicamento;
import org.example.service.FavoritoService;
import org.example.service.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/medicamentos") // Ruta base para este controlador web
public class MedicamentoWebController {

    @Autowired
    private MedicamentoService medicamentoService;

    @Autowired
    private FavoritoService favoritoService;

    @GetMapping("/catalogo")
    public String mostrarCatalogo(Model model,
                                  @RequestParam(required = false) String busqueda,
                                  @RequestParam(required = false) Double latitud,
                                  @RequestParam(required = false) Double longitud) {
        List<Medicamento> medicamentos;
        if (busqueda != null && !busqueda.isEmpty()) {
            if (latitud != null && longitud != null) {
                medicamentos = medicamentoService.buscarPorNombreYUbicacion(busqueda, latitud, longitud);
                model.addAttribute("latitud", latitud);
                model.addAttribute("longitud", longitud);
            } else {
                medicamentos = medicamentoService.buscarPorNombre(busqueda);
            }
            model.addAttribute("busquedaActual", busqueda);
        } else {
            if (latitud != null && longitud != null) {
                medicamentos = medicamentoService.buscarPorUbicacion(latitud, longitud);
                model.addAttribute("latitud", latitud);
                model.addAttribute("longitud", longitud);
            } else {
                medicamentos = medicamentoService.obtenerTodos();
            }
        }
        model.addAttribute("medicamentos", medicamentos);

        // --- 2. LÓGICA PARA OBTENER Y PASAR LOS FAVORITOS A LA VISTA ---

        // Obtenemos el contexto de seguridad actual para saber quién está logueado.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Comprobamos si hay un usuario válido y no es el "usuario anónimo" por defecto.
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            // Si hay un usuario, obtenemos su nombre de usuario.
            String username = auth.getName();

            // Usamos el servicio para obtener el conjunto de IDs de sus medicamentos favoritos.
            Set<String> favoritosIds = favoritoService.obtenerIdsMedicamentosFavoritos(username);

            // Añadimos este conjunto al modelo. La vista (Thymeleaf) usará esta lista
            // para decidir si muestra el botón "Añadir" o "Quitar".
            model.addAttribute("favoritosIds", favoritosIds);
        } else {
            // Si no hay nadie logueado, pasamos un conjunto vacío.
            // Esto es una buena práctica para evitar errores en la plantilla si intenta
            // acceder a la variable 'favoritosIds' y esta no existe.
            model.addAttribute("favoritosIds", Collections.emptySet());
        }

        return "medicamentos/catalogo"; // Devolvemos el nombre de la vista
    }
}