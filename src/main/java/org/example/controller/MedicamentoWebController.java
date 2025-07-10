package org.example.controller;

import org.example.model.Medicamento;
import org.example.service.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.example.model.Favorito;
import org.example.model.Usuario;
import org.example.service.FavoritoService;
import org.example.service.UsuarioService;

@Controller
public class MedicamentoWebController {

    private final MedicamentoService medicamentoService;
    private final FavoritoService favoritoService;
    private final UsuarioService usuarioService;

    @Autowired
    public MedicamentoWebController(MedicamentoService medicamentoService, FavoritoService favoritoService, UsuarioService usuarioService) {
        this.medicamentoService = medicamentoService;
        this.favoritoService = favoritoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/medicamentos/catalogo")
    public String catalogo(Model model) {
        List<Medicamento> medicamentos = medicamentoService.obtenerTodos();
        model.addAttribute("medicamentos", medicamentos);
        return "medicamentos/catalogo";
    }

    @GetMapping("/medicamentos/detalle/{id}")
    public String detalle(@PathVariable("id") String id, Model model, Principal principal) {
        Optional<Medicamento> medicamentoOpt = medicamentoService.findById(id);
        if (medicamentoOpt.isPresent()) {
            Medicamento medicamento = medicamentoOpt.get();
            model.addAttribute("medicamento", medicamento);

            boolean esFavorito = false;
            if (principal != null) {
                String username = principal.getName();
                Optional<Usuario> usuarioOpt = usuarioService.findByUsername(username);
                if (usuarioOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();
                    List<Favorito> favoritos = favoritoService.obtenerFavoritosPorUsuario(usuario.getId());
                    esFavorito = favoritos.stream()
                        .anyMatch(fav -> fav.getMedicamento().getId().equals(medicamento.getId()));
                }
            }
            model.addAttribute("esFavorito", esFavorito);

            return "medicamentos/detalle";
        } else {
            return "error/404";
        }
    }
}
