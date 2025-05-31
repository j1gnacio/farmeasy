package org.example.service;

import org.example.model.Medicamento;
import org.example.repository.MedicamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoService(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    public List<Medicamento> obtenerTodos() {
        return medicamentoRepository.findAll();
    }

    public Medicamento guardar(Medicamento medicamento) {
        return medicamentoRepository.save(medicamento);
    }

    public List<Medicamento> buscarPorNombre(String nombre) {
        return medicamentoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Medicamento> buscarPorFarmacia(String farmacia) {
        return medicamentoRepository.findByFarmaciaIgnoreCase(farmacia);
    }
}
