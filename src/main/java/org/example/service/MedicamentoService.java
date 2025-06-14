package org.example.service;

import org.example.dto.MedicamentoDisplayDTO;
import org.example.model.Medicamento;
import org.example.model.PrecioInfo;
import org.example.repository.MedicamentoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Medicamento> buscarPorNombre(String nombreDisplay) {
        return medicamentoRepository.findByNombreDisplayContainingIgnoreCase(nombreDisplay);
    }


    public List<MedicamentoDisplayDTO> buscarParaCatalogo(String nombreBusqueda) {
        // 1. Obtener los medicamentos de la base de datos
        List<Medicamento> medicamentosRepo;
        if (nombreBusqueda != null && !nombreBusqueda.isEmpty()) {
            medicamentosRepo = medicamentoRepository.findByNombreDisplayContainingIgnoreCase(nombreBusqueda);
        } else {
            medicamentosRepo = medicamentoRepository.findAll();
        }

        // 2. "Aplanar" la lista para la vista
        List<MedicamentoDisplayDTO> catalogo = new ArrayList<>();
        for (Medicamento med : medicamentosRepo) {
            for (PrecioInfo precio : med.getPrecios()) {
                // Para cada precio de cada medicamento, creamos un objeto para mostrar
                catalogo.add(new MedicamentoDisplayDTO(
                        med.getNombreDisplay(),
                        med.getDescripcion(),
                        med.getImagenUrl(),
                        precio.getFarmaciaNombre(),
                        precio.getPrecioInternet(),
                        precio.getPrecioFisico(),
                        precio.getUrlProducto()
                ));
            }
        }
        return catalogo;
    }
}
