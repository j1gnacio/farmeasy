package cl.farmeasy.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import cl.farmeasy.repository.MedicamentoRepository;
import cl.farmeasy.model.Medicamento;
import org.springframework.stereotype.Service;

/**
 * Servicio para la logica de negocio relacionada con los medicamentos.
 */
@Service
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    /**
     * Constructor para inyectar las dependencias del servicio de medicamentos.
     *
     * @param medicamentoRepository El repositorio para el acceso a datos de medicamentos.
     */
    public MedicamentoService(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    /**
     * Obtiene todos los medicamentos del repositorio.
     *
     * @return Una lista de todos los medicamentos.
     */
    public List<Medicamento> obtenerTodos() {
        return medicamentoRepository.findAll();
    }

    /**
     * Busca un medicamento especifico por su ID.
     *
     * @param id El ID del medicamento a buscar.
     * @return Un Optional que puede contener el medicamento si se encuentra.
     */
    public Optional<Medicamento> findById(String id) {
        return medicamentoRepository.findById(id);
    }

    /**
     * Guarda un medicamento en el repositorio.
     *
     * @param medicamento El medicamento a guardar.
     * @return El medicamento guardado.
     */
    public Medicamento guardar(Medicamento medicamento) {
        return medicamentoRepository.save(medicamento);
    }

    /**
     * Busca medicamentos por su nombre, ignorando mayusculas y minusculas.
     *
     * @param nombre El nombre del medicamento a buscar.
     * @return Una lista de medicamentos que coinciden con el nombre.
     */
    public List<Medicamento> buscarPorNombre(String nombre) {
        return medicamentoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Busca medicamentos segun los criterios proporcionados.
     * Maneja busquedas por nombre, por farmacia, o por ambos.
     *
     * @param nombre El nombre del medicamento (puede ser nulo o vacio).
     * @param farmacia La farmacia para filtrar (puede ser "TODAS" para no aplicar filtro).
     * @return Una lista de medicamentos que cumplen con los filtros.
     */
    public List<Medicamento> buscarMedicamentos(String nombre, String farmacia) {
        boolean tieneNombre = nombre != null && !nombre.trim().isEmpty();
        boolean tieneFarmaciaEspecifica = farmacia != null && !farmacia.trim().isEmpty() && !"TODAS".equalsIgnoreCase(farmacia);

        // Caso 1: Buscar por nombre y filtrar por farmacia
        if (tieneNombre && tieneFarmaciaEspecifica) {
            return medicamentoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                    .filter(m -> farmacia.equalsIgnoreCase(m.getFarmacia()))
                    .collect(Collectors.toList());
        }
        // Caso 2: Buscar solo por nombre
        else if (tieneNombre) {
            return medicamentoRepository.findByNombreContainingIgnoreCase(nombre);
        }
        // Caso 3: Buscar solo por farmacia
        else if (tieneFarmaciaEspecifica) {
            return medicamentoRepository.findByFarmaciaIgnoreCase(farmacia);
        }
        // Caso 4: No hay filtros de busqueda, devolver todos los medicamentos
        else {
            return medicamentoRepository.findAll();
        }
    }

    /**
     * Obtiene una lista de todos los nombres de farmacias unicos para los filtros.
     *
     * @return Una lista de Strings con los nombres de las farmacias.
     */
    public List<String> obtenerNombresDeFarmacias() {
        return medicamentoRepository.findAll().stream()
                .map(Medicamento::getFarmacia)
                .filter(f -> f != null && !f.trim().isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}