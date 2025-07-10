package cl.farmeasy.service;

import cl.farmeasy.repository.FavoritoRepository;
import cl.farmeasy.repository.MedicamentoRepository;
import cl.farmeasy.repository.UsuarioRepository;
import cl.farmeasy.model.Favorito;
import cl.farmeasy.model.Medicamento;
import cl.farmeasy.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar la lógica de negocio de los medicamentos favoritos.
 */
@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    /**
     * Agrega un medicamento a la lista de favoritos de un usuario.
     *
     * @param username El nombre de usuario.
     * @param medicamentoId El ID del medicamento.
     * @throws IllegalStateException si el medicamento ya es un favorito.
     */
    @Transactional
    public void agregarFavorito(String username, String medicamentoId) {
        // 1. Buscar el usuario y el medicamento
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        Medicamento medicamento = medicamentoRepository.findById(medicamentoId)
                .orElseThrow(() -> new NoSuchElementException("Medicamento no encontrado: " + medicamentoId));

        // 2. Verificar si ya es un favorito para evitar duplicados
        if (favoritoRepository.findByUsuarioIdAndMedicamentoId(usuario.getId(), medicamento.getId()).isPresent()) {
            // Ya es un favorito, no hacemos nada o lanzamos una excepción
            throw new IllegalStateException("Este medicamento ya está en tus favoritos.");
        }

        // 3. Crear y guardar el nuevo favorito
        Favorito nuevoFavorito = new Favorito(usuario, medicamento);
        favoritoRepository.save(nuevoFavorito);

        // 4. (Opcional, pero bueno) Actualizar la lista en el objeto Usuario
        usuario.getFavoritos().add(nuevoFavorito);
        usuarioRepository.save(usuario);
    }

    /**
     * Elimina un medicamento de la lista de favoritos de un usuario.
     *
     * @param username El nombre de usuario.
     * @param medicamentoId El ID del medicamento.
     */
    @Transactional
    public void eliminarFavorito(String username, String medicamentoId) {
        // 1. Buscar el usuario
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // 2. Eliminar el favorito directamente por los IDs
        favoritoRepository.deleteByUsuarioIdAndMedicamentoId(usuario.getId(), medicamentoId);

        // 3. (Opcional, pero bueno) También eliminar de la lista del usuario
        usuario.getFavoritos().removeIf(fav -> fav.getMedicamento().getId().equals(medicamentoId));
        usuarioRepository.save(usuario);
    }

    /**
     * Obtiene todos los favoritos de un usuario.
     *
     * @param username El nombre de usuario.
     * @return Una lista de los favoritos del usuario.
     */
    public List<Favorito> obtenerFavoritosPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        return favoritoRepository.findByUsuarioId(usuario.getId());
    }

    /**
     * Verifica si un medicamento es favorito de un usuario.
     *
     * @param username El nombre de usuario.
     * @param medicamentoId El ID del medicamento.
     * @return true si es un favorito, false en caso contrario.
     */
    public boolean esFavorito(String username, String medicamentoId) {
        // Método útil para la vista, para saber si mostrar "Agregar" o "Eliminar"
        return usuarioRepository.findByUsername(username)
                .flatMap(usuario -> favoritoRepository.findByUsuarioIdAndMedicamentoId(usuario.getId(), medicamentoId))
                .isPresent();
    }


    /**
     * Obtiene los IDs de todos los medicamentos favoritos de un usuario.
     *
     * @param username El nombre de usuario.
     * @return Un conjunto de IDs de medicamentos.
     */
    public Set<String> obtenerIdsMedicamentosFavoritos(String username) {
        // Obtenemos al usuario
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Obtenemos su lista de favoritos
        List<Favorito> favoritos = favoritoRepository.findByUsuarioId(usuario.getId());

        // Usamos streams para extraer solo el ID de cada medicamento y lo guardamos en un Set
        return favoritos.stream()
                .map(favorito -> favorito.getMedicamento().getId())
                .collect(Collectors.toSet());
    }
}