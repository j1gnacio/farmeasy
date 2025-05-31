package org.example.controller;

import org.example.model.Medicamento;
import org.example.repository.MedicamentoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @GetMapping("/buscar")
    public List<Medicamento> buscarPorNombre(@RequestParam String nombre) {
        return repo.findByNombreContainingIgnoreCase(nombre);
    }

    // Nuevo endpoint para obtener lista única de farmacias
    @GetMapping("/farmacias")
    public ResponseEntity<List<String>> obtenerFarmaciasUnicas() {
        List<String> farmacias = repo.findAll()
                .stream()
                .map(Medicamento::getFarmacia)
                .filter(Objects::nonNull)
                .map(String::trim)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        return ResponseEntity.ok(farmacias);
    }

    // Nuevo endpoint para buscar medicamentos por farmacia
    @GetMapping("/farmacia")
    public ResponseEntity<List<Medicamento>> buscarPorFarmacia(@RequestParam String nombre) {
        List<Medicamento> medicamentos = repo.findByFarmaciaIgnoreCase(nombre);
        return ResponseEntity.ok(medicamentos);
    }
}
