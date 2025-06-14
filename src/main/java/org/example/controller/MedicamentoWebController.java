// Archivo: src/main/java/org/example/controller/MedicamentoWebController.java
package org.example.controller;

import org.example.dto.MedicamentoDisplayDTO; // Importa el DTO
import org.example.service.MedicamentoService; // Importa el Servicio
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/medicamentos")
public class MedicamentoWebController {

    @Autowired
    private MedicamentoService medicamentoService; // Inyecta el SERVICIO, no el repositorio

    @GetMapping("/catalogo")
    public String mostrarCatalogo(Model model, @RequestParam(required = false) String busqueda) {

        // Llama al método del servicio que devuelve la lista de DTOs
        List<MedicamentoDisplayDTO> medicamentosParaVista = medicamentoService.buscarParaCatalogo(busqueda);

        // Pasa la lista correcta (de DTOs) a la vista
        model.addAttribute("medicamentos", medicamentosParaVista);

        if (busqueda != null && !busqueda.isEmpty()) {
            model.addAttribute("busquedaActual", busqueda);
        }

        return "medicamentos/catalogo";
    }
}