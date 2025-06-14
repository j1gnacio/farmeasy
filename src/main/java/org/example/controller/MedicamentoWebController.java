package org.example.controller;

import java.util.List;

import org.example.model.Medicamento;
import org.example.service.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return "medicamentos/catalogo"; // Vista catalogo.html
    }

    // Puedes añadir más métodos aquí para otras vistas web de medicamentos
    // por ejemplo, ver detalle de un medicamento, etc.
}