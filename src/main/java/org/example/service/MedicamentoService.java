package org.example.service;

import java.util.ArrayList;
import java.util.List;

import org.example.model.Farmacia;
import org.example.model.Medicamento;
import org.example.repository.MedicamentoRepository;
import org.springframework.stereotype.Service;

@Service
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;
    private final FarmaciaService farmaciaService;

    public MedicamentoService(MedicamentoRepository medicamentoRepository, FarmaciaService farmaciaService) {
        this.medicamentoRepository = medicamentoRepository;
        this.farmaciaService = farmaciaService;
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

    public List<Medicamento> buscarPorUbicacion(double latitud, double longitud) {
        // Buscar farmacias cercanas en un radio de 5 km (5000 metros)
        List<Farmacia> farmaciasCercanas = farmaciaService.buscarFarmaciasCercanas(latitud, longitud, 5000);
        List<Medicamento> medicamentos = new ArrayList<>();
        for (Farmacia farmacia : farmaciasCercanas) {
            if (farmacia.getListaMedicamentos() != null) {
                medicamentos.addAll(farmacia.getListaMedicamentos());
            }
        }
        return medicamentos;
    }

    public List<Medicamento> buscarPorNombreYUbicacion(String nombre, double latitud, double longitud) {
        List<Medicamento> medicamentosPorNombre = buscarPorNombre(nombre);
        List<Medicamento> medicamentosPorUbicacion = buscarPorUbicacion(latitud, longitud);
        List<Medicamento> resultado = new ArrayList<>();
        for (Medicamento med : medicamentosPorNombre) {
            if (medicamentosPorUbicacion.contains(med)) {
                resultado.add(med);
            }
        }
        return resultado;
    }
}
