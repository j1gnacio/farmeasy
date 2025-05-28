package org.example.service;

import org.example.model.Medicamento;
import org.example.repository.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository repository;

    public List<Medicamento> obtenerTodos() {
        return repository.findAll();
    }

    public List<Medicamento> buscarPorNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Medicamento> buscarPorFarmacia(String farmacia) {
        return repository.findByFarmacia(farmacia);
    }

    public Medicamento guardar(Medicamento medicamento) {
        return repository.save(medicamento);
    }

}
