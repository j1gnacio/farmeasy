package org.example.controller;

import org.example.model.Medicamento;
import org.example.security.UserDetailsServiceImpl;
import org.example.service.FavoritoService;
import org.example.service.HistorialBusquedaService;
import org.example.service.MedicamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Indicamos a Spring que este es un test de la capa web, enfocado solo en MedicamentoWebController.
@WebMvcTest(MedicamentoWebController.class)
class MedicamentoWebControllerTest {

    @Autowired
    private MockMvc mockMvc; // Objeto para simular peticiones HTTP (GET, POST, etc.)

    // Creamos mocks de los servicios que este controlador utiliza.
    // Spring reemplazará los beans reales con estos mocks durante el test.
    @MockBean
    private MedicamentoService medicamentoService;
    @MockBean
    private FavoritoService favoritoService;
    @MockBean
    private HistorialBusquedaService historialBusquedaService;

    // Los tests de controladores protegidos por Spring Security necesitan un mock del UserDetailsService.
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private Medicamento medicamento;

    @BeforeEach
    void setUp() {
        medicamento = new Medicamento();
        medicamento.setId("med1");
        medicamento.setNombre("Test-Med");
    }

    // --- Tests para el endpoint /catalogo ---

//    @Test
//    void cuandoPideCatalogo_sinBusqueda_debeDevolverTodosLosMedicamentos() throws Exception {
//        // Arrange
//        when(medicamentoService.obtenerTodos()).thenReturn(List.of(medicamento));
//        when(favoritoService.obtenerIdsMedicamentosFavoritos(any())).thenReturn(Collections.emptySet());
//
//        // Act & Assert
//        mockMvc.perform(get("/medicamentos/catalogo"))
//                .andExpect(status().isOk()) // Esperamos un código de respuesta 200 OK
//                .andExpect(view().name("medicamentos/catalogo")) // Esperamos que se renderice la vista correcta
//                .andExpect(model().attributeExists("medicamentos")) // Esperamos que el modelo contenga la lista de medicamentos
//                .andExpect(model().attribute("medicamentos", hasSize(1))); // Verificamos que la lista tiene 1 elemento
//
//        verify(medicamentoService).obtenerTodos(); // Verificamos que se llamó al método correcto del servicio
//        verify(medicamentoService, never()).buscarPorNombre(anyString()); // Verificamos que NO se llamó a buscarPorNombre
//    }

    @Test
    @WithMockUser(username = "testuser") // Simula que un usuario llamado "testuser" ha iniciado sesión
    void cuandoPideCatalogo_conBusqueda_debeBuscarPorNombreYGuardarHistorial() throws Exception {
        // Arrange
        String terminoBusqueda = "Test";
        when(medicamentoService.buscarPorNombre(terminoBusqueda)).thenReturn(List.of(medicamento));
        when(favoritoService.obtenerIdsMedicamentosFavoritos("testuser")).thenReturn(Set.of("med1"));

        // Act & Assert
        mockMvc.perform(get("/medicamentos/catalogo").param("busqueda", terminoBusqueda))
                .andExpect(status().isOk())
                .andExpect(view().name("medicamentos/catalogo"))
                .andExpect(model().attributeExists("busquedaActual", "medicamentos", "favoritosIds"))
                .andExpect(model().attribute("favoritosIds", hasSize(1)));

        // Verificamos que se llamaron los métodos correctos de los servicios
        verify(medicamentoService).buscarPorNombre(terminoBusqueda);
        verify(historialBusquedaService).guardarBusqueda(terminoBusqueda, "testuser");
        verify(medicamentoService, never()).obtenerTodos();
    }

    // --- Tests para el endpoint /medicamentos/{id} ---

    @Test
    @WithMockUser(username = "testuser")
    void cuandoPideDetalle_conIdValido_debeDevolverPaginaDeDetalle() throws Exception {
        // Arrange
        when(medicamentoService.findById("med1")).thenReturn(Optional.of(medicamento));
        when(medicamentoService.buscarPorNombre("Test-Med")).thenReturn(List.of(medicamento));
        when(favoritoService.esFavorito("testuser", "med1")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/medicamentos/{id}", "med1"))
                .andExpect(status().isOk())
                .andExpect(view().name("medicamentos/detalle"))
                .andExpect(model().attribute("medicamento", medicamento))
                .andExpect(model().attribute("esFavorito", true))
                .andExpect(model().attribute("comparativaPrecios", hasSize(1)));

        verify(medicamentoService).findById("med1");
        verify(favoritoService).esFavorito("testuser", "med1");
    }

//    @Test
//    void cuandoPideDetalle_conIdInvalido_debeLanzarExcepcion() throws Exception {
//        // Arrange
//        // Simulamos que el servicio no encuentra el medicamento y devuelve un Optional vacío
//        when(medicamentoService.findById("id_no_existe")).thenReturn(Optional.empty());
//
//        // Act & Assert
//        // Verificamos que la petición resulta en una excepción de tipo RuntimeException,
//        // lo que Spring normalmente convierte en un error 500.
//        mockMvc.perform(get("/medicamentos/{id}", "id_no_existe"))
//                .andExpect(status().isInternalServerError())
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
//                .andExpect(result -> assertEquals("Medicamento no encontrado con ID: id_no_existe", result.getResolvedException().getMessage()));
//    }
}