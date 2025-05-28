package org.example.controller;

import org.example.model.Medicamento;
import org.example.service.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
@CrossOrigin(origins = "*") // Habilita peticiones desde cualquier frontend (útil para pruebas)
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    // Obtener todos los medicamentos
    @GetMapping
    public List<Medicamento> obtenerTodos() {
        return medicamentoService.obtenerTodos();
    }

    // Buscar por nombre (ej: /api/medicamentos/nombre/paracetamol)
    @GetMapping("/nombre/{nombre}")
    public List<Medicamento> buscarPorNombre(@PathVariable String nombre) {
        return medicamentoService.buscarPorNombre(nombre);
    }

    // Buscar por farmacia (ej: /api/medicamentos/farmacia/salcobrand)
    @GetMapping("/farmacia/{farmacia}")
    public List<Medicamento> buscarPorFarmacia(@PathVariable String farmacia) {
        return medicamentoService.buscarPorFarmacia(farmacia);
    }

    // Guardar un nuevo medicamento
    @PostMapping
    public Medicamento guardar(@RequestBody Medicamento medicamento) {
        return medicamentoService.guardar(medicamento);
    }
}
