package org.example.controller;

import org.example.model.HistorialBusqueda;
import org.example.repository.HistorialBusquedaRepository;
import org.example.repository.UsuarioRepository;
import org.example.service.HistorialBusquedaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class HistorialControllerIntegrationTest {

    @Mock
    private HistorialBusquedaRepository historialBusquedaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private HistorialBusquedaService historialBusquedaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerHistorialPorUsuario() {
        String usuarioId = "usuario1";
        List<HistorialBusqueda> mockHistorial = List.of(
                new HistorialBusqueda("busqueda1", null),
                new HistorialBusqueda("busqueda2", null)
        );

        when(historialBusquedaRepository.findByUsuarioId(usuarioId, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "fechaBusqueda"))).thenReturn(mockHistorial);
        when(usuarioRepository.findByUsername(usuarioId)).thenReturn(java.util.Optional.of(new org.example.model.Usuario()));

        List<HistorialBusqueda> resultado = historialBusquedaService.obtenerHistorialPorUsuario(usuarioId);

        assertEquals(2, resultado.size());
        try {
            java.lang.reflect.Field field = resultado.get(0).getClass().getDeclaredField("terminoBuscado");
            field.setAccessible(true);
            String terminoBuscado = (String) field.get(resultado.get(0));
            org.junit.jupiter.api.Assertions.assertEquals("busqueda1", terminoBuscado);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
