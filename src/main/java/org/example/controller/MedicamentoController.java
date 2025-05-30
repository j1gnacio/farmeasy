package org.example.controller;

import org.example.model.Medicamento;
import org.example.repository.MedicamentoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoController {

    private final MedicamentoRepository repo;

    public MedicamentoController(MedicamentoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Medicamento> obtenerTodos() {
        return repo.findAll();
    }

    @PostMapping
    public Medicamento guardarMedicamento(@RequestBody Medicamento medicamento) {
        return repo.save(medicamento);
    }
}
