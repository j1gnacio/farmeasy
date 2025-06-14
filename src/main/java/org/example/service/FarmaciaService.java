package org.example.service;

import java.util.List;

import org.example.model.Farmacia;
import org.example.repository.FarmaciaRepository;
import org.springframework.stereotype.Service;

@Service
public class FarmaciaService {

    private final FarmaciaRepository farmaciaRepository;

    public FarmaciaService(FarmaciaRepository farmaciaRepository) {
        this.farmaciaRepository = farmaciaRepository;
    }

    /**
     * Busca farmacias cercanas a una ubicación dada dentro de un radio en metros.
     * @param latitud Latitud del punto central.
     * @param longitud Longitud del punto central.
     * @param radio Radio de búsqueda en metros.
     * @return Lista de farmacias cercanas.
     */
    public List<Farmacia> buscarFarmaciasCercanas(double latitud, double longitud, double radio) {
        // Nota: El orden de coordenadas en MongoDB es [longitud, latitud]
        return farmaciaRepository.findByUbicacionNear(longitud, latitud, radio);
    }

    public List<Farmacia> obtenerTodas() {
        return farmaciaRepository.findAll();
    }

    public Farmacia guardar(Farmacia farmacia) {
        return farmaciaRepository.save(farmacia);
    }

    public List<Farmacia> buscarPorNombre(String nombre) {
        return farmaciaRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
