package org.example.controller;

import jakarta.validation.Valid;
import org.example.config.ViewNames;
import org.example.exception.RegistroException;
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

/**
 * Controlador para gestionar la autenticación de usuarios, incluyendo el inicio de sesión y el registro.
 */
@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Muestra la página de inicio de sesión.
     *
     * @param model El modelo para pasar datos a la vista.
     * @param error Indica si ocurrió un error en el último intento de inicio de sesión.
     * @param logout Indica si el usuario acaba de cerrar sesión.
     * @return La vista de la página de inicio de sesión.
     */
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

    /**
     * Muestra la página de registro de nuevos usuarios.
     *
     * @param model El modelo para pasar datos a la vista.
     * @return La vista de la página de registro.
     */
    @GetMapping(ViewNames.REGISTRO_URL)
    public String registroPage(Model model) {
        model.addAttribute("usuario", new Usuario());
        return ViewNames.REGISTRO_VIEW;
    }

    /**
     * Procesa el formulario de registro de un nuevo usuario.
     *
     * @param usuario El objeto Usuario con los datos del formulario.
     * @param result El resultado del binding para la validación.
     * @param model El modelo para pasar datos a la vista en caso de error.
     * @param redirectAttributes Atributos para pasar mensajes entre redirecciones.
     * @return Redirige a la página de inicio de sesión si el registro es exitoso, o vuelve a la vista de registro si hay errores.
     */
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
        } catch (RegistroException e) { // <-- Se captura la excepción específica
            model.addAttribute("errorMessage", e.getMessage());
            return ViewNames.REGISTRO_VIEW;
        }
    }
}