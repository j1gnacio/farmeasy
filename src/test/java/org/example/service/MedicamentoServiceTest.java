package org.example.service;

import org.example.model.Medicamento;
import org.example.repository.MedicamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicamentoServiceTest {

    @Mock
    private MedicamentoRepository medicamentoRepository;

    @InjectMocks
    private MedicamentoService medicamentoService;

    private Medicamento medicamento1;
    private Medicamento medicamento2;

    @BeforeEach
    void setUp() {
        // Preparamos datos de prueba
        medicamento1 = new Medicamento();
        medicamento1.setId("med1");
        medicamento1.setNombre("Paracetamol 500mg");

        medicamento2 = new Medicamento();
        medicamento2.setId("med2");
        medicamento2.setNombre("Ibuprofeno 200mg");
    }

    // --- Test para obtenerTodos ---
    @Test
    void cuandoObtieneTodos_debeDevolverListaDeMedicamentos() {
        // Arrange (Preparación)
        // Simulamos que el repositorio devuelve una lista con nuestros dos medicamentos de prueba.
        when(medicamentoRepository.findAll()).thenReturn(List.of(medicamento1, medicamento2));

        // Act (Ejecución)
        List<Medicamento> resultado = medicamentoService.obtenerTodos();

        // Assert (Verificación)
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Paracetamol 500mg", resultado.get(0).getNombre());
        verify(medicamentoRepository).findAll(); // Verificamos que se llamó al método findAll() del repo.
    }

    // --- Tests para findById ---
    @Test
    void cuandoBuscaPorIdExistente_debeDevolverOptionalConMedicamento() {
        // Arrange
        // Simulamos que al buscar por "med1", el repositorio encuentra y devuelve nuestro medicamento1.
        when(medicamentoRepository.findById("med1")).thenReturn(Optional.of(medicamento1));

        // Act
        Optional<Medicamento> resultado = medicamentoService.findById("med1");

        // Assert
        assertTrue(resultado.isPresent()); // Verificamos que el Optional no está vacío.
        assertEquals("Paracetamol 500mg", resultado.get().getNombre());
        verify(medicamentoRepository).findById("med1");
    }

    @Test
    void cuandoBuscaPorIdInexistente_debeDevolverOptionalVacio() {
        // Arrange
        // Simulamos que al buscar por un ID que no existe, el repositorio devuelve un Optional vacío.
        when(medicamentoRepository.findById("id_no_existe")).thenReturn(Optional.empty());

        // Act
        Optional<Medicamento> resultado = medicamentoService.findById("id_no_existe");

        // Assert
        assertFalse(resultado.isPresent()); // Verificamos que el Optional está vacío.
        verify(medicamentoRepository).findById("id_no_existe");
    }

    // --- Test para guardar ---
    @Test
    void cuandoGuardaMedicamento_debeDevolverMedicamentoGuardado() {
        // Arrange
        // Simulamos que cuando se llama a save con cualquier objeto Medicamento, devuelve ese mismo objeto.
        when(medicamentoRepository.save(any(Medicamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Medicamento resultado = medicamentoService.guardar(medicamento1);

        // Assert
        assertNotNull(resultado);
        assertEquals("med1", resultado.getId());
        verify(medicamentoRepository).save(medicamento1);
    }

    // --- Test para buscarPorNombre ---
    @Test
    void cuandoBuscaPorNombre_debeDevolverListaCoincidente() {
        // Arrange
        String terminoBusqueda = "parace";
        // Simulamos que al buscar por "parace", el repositorio devuelve una lista que contiene solo a paracetamol.
        when(medicamentoRepository.findByNombreContainingIgnoreCase(terminoBusqueda)).thenReturn(List.of(medicamento1));

        // Act
        List<Medicamento> resultado = medicamentoService.buscarPorNombre(terminoBusqueda);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Paracetamol 500mg", resultado.get(0).getNombre());
        verify(medicamentoRepository).findByNombreContainingIgnoreCase(terminoBusqueda);
    }
}