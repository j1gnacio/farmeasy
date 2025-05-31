package org.example.service;

import org.example.model.Farmacia;
import org.example.repository.FarmaciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FarmaciaService {

    private final FarmaciaRepository repo;

    public FarmaciaService(FarmaciaRepository repo) {
        this.repo = repo;
    }

    public List<Farmacia> obtenerTodas() {
        return repo.findAll();
    }

    public Farmacia guardar(Farmacia farmacia) {
        return repo.save(farmacia);
    }

    public List<Farmacia> buscarPorNombre(String nombre) {
        return repo.findByNombreContainingIgnoreCase(nombre);
    }
}
