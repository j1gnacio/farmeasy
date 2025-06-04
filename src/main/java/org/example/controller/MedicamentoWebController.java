package org.example.controller;

import org.example.model.Medicamento;
import org.example.repository.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/medicamentos") // Ruta base para este controlador web
public class MedicamentoWebController {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @GetMapping("/catalogo")
    public String mostrarCatalogo(Model model, @RequestParam(required = false) String busqueda) {
        List<Medicamento> medicamentos;
        if (busqueda != null && !busqueda.isEmpty()) {
            medicamentos = medicamentoRepository.findByNombreContainingIgnoreCase(busqueda);
            model.addAttribute("busquedaActual", busqueda);
        } else {
            medicamentos = medicamentoRepository.findAll();
        }
        model.addAttribute("medicamentos", medicamentos);
        return "medicamentos/catalogo"; // Vista catalogo.html
    }

    // Puedes añadir más métodos aquí para otras vistas web de medicamentos
    // por ejemplo, ver detalle de un medicamento, etc.
}