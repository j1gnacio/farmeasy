package org.example.service;

import java.util.List;
import java.util.Optional;

import org.example.model.Medicamento;
import org.example.repository.MedicamentoRepository;
import org.springframework.stereotype.Service;

@Service
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    // Se elimina la dependencia de FarmaciaService para simplificar el proyecto.
    public MedicamentoService(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    public List<Medicamento> obtenerTodos() {
        return medicamentoRepository.findAll();
    }

    /**
     * Busca un medicamento específico por su ID.
     * Devuelve un Optional, que puede contener el medicamento si se encuentra, o estar vacío si no.
     * @param id El ID del medicamento a buscar.
     * @return Un Optional<Medicamento>.
     */
    public Optional<Medicamento> findById(String id) {
        return medicamentoRepository.findById(id);
    }

    public Medicamento guardar(Medicamento medicamento) {
        return medicamentoRepository.save(medicamento);
    }

    public List<Medicamento> buscarPorNombre(String nombre) {
        return medicamentoRepository.findByNombreContainingIgnoreCase(nombre);
    }

}