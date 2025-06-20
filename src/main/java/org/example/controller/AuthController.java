package org.example.controller;

import jakarta.validation.Valid;
import org.example.config.ViewNames;
import org.example.model.Usuario;
import org.example.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping(ViewNames.LOGIN_URL)
    public String loginPage(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("errorMessage", "Usuario o contraseña incorrectos.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Has cerrado sesión exitosamente.");
        }
        return ViewNames.LOGIN_VIEW;
    }

    @GetMapping(ViewNames.REGISTRO_URL)
    public String registroPage(Model model) {
        model.addAttribute("usuario", new Usuario());
        return ViewNames.REGISTRO_VIEW;
    }

    @PostMapping(ViewNames.REGISTRO_URL)
    public String registrarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
                                   BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return ViewNames.REGISTRO_VIEW;
        }
        try {
            usuarioService.registrarUsuario(usuario);
            redirectAttributes.addFlashAttribute("successMessage", "¡Registro exitoso! Por favor, inicia sesión.");
            return ViewNames.REDIRECT_LOGIN;
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return ViewNames.REGISTRO_VIEW;
        }
    }
}