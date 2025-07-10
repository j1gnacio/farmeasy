package org.example.service;

import java.lang.reflect.Field;
import java.util.List;

import org.example.model.Favorito;
import org.example.model.Medicamento;
import org.example.model.Usuario;
import org.example.repository.FavoritoRepository;
import org.example.repository.UsuarioRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class FavoritoServiceTest {

    @Mock
    private FavoritoRepository favoritoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private FavoritoService favoritoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Usuario usuarioMock = new Usuario();
        setPrivateField(usuarioMock, "id", "usuario1");
        when(usuarioRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(usuarioMock));
    }

    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testObtenerFavoritosPorUsuario() {
        String usuarioId = "usuario1";
        Usuario usuario = new Usuario();
        setPrivateField(usuario, "id", usuarioId);
        Medicamento medicamento = new Medicamento("idMed", "nombre", "desc", "marca", 0.0, 0.0, "categoria", "presentacion");

        List<Favorito> mockFavoritos = List.of(
                new Favorito(usuario, medicamento),
                new Favorito(usuario, medicamento)
        );

        when(favoritoRepository.findByUsuarioId(usuarioId)).thenReturn(mockFavoritos);

        List<Favorito> resultado = favoritoService.obtenerFavoritosPorUsuario(usuarioId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(favoritoRepository, times(1)).findByUsuarioId(usuarioId);
    }
}
