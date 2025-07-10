package cl.farmeasy.controller; // <-- Paquete corregido

import cl.farmeasy.config.ViewNames; // <-- Import actualizado
import cl.farmeasy.exception.RegistroException; // <-- Import actualizado
import cl.farmeasy.service.UsuarioService; // <-- Import actualizado
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

/**
 * Controlador para gestionar las acciones del perfil de usuario,
 * como cambiar la contrasena.
 */
@Controller
@RequestMapping("/perfil")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Muestra la pagina para cambiar la contrasena.
     *
     * @return La vista del formulario para cambiar la contrasena.
     */
    @GetMapping("/cambiar-password")
    public String mostrarFormularioCambioPassword() {
        return "perfil/cambiar-password";
    }

    /**
     * Procesa la solicitud para cambiar la contrasena.
     *
     * @param oldPassword La contrasena actual del usuario.
     * @param newPassword La nueva contrasena.
     * @param confirmPassword La confirmacion de la nueva contrasena.
     * @param principal El objeto de seguridad del usuario autenticado.
     * @param redirectAttributes Atributos para mensajes flash.
     * @param model El modelo para pasar datos a la vista en caso de error.
     * @return Redirige al catalogo con mensaje de exito o vuelve al formulario con un error.
     */
    @PostMapping("/cambiar-password")
    public String procesarCambioPassword(@RequestParam String oldPassword,
                                         @RequestParam String newPassword,
                                         @RequestParam String confirmPassword,
                                         Principal principal,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Las nuevas contrasenas no coinciden.");
            return "perfil/cambiar-password";
        }
        if (newPassword.length() < 6) {
            model.addAttribute("errorMessage", "La nueva contrasena debe tener al menos 6 caracteres.");
            return "perfil/cambiar-password";
        }

        try {
            usuarioService.cambiarPassword(principal.getName(), oldPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "¡Contrasena actualizada correctamente!");
            return "redirect:" + ViewNames.CATALOGO_URL;
        } catch (RegistroException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "perfil/cambiar-password";
        }
    }
}