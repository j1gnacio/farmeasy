package org.example.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa a un usuario del sistema.
 * Contiene información de autenticación y las listas de medicamentos favoritos.
 */
@Document(collection = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    private String id;

    /**
     * El nombre de usuario, debe ser único y tener entre 3 y 20 caracteres.
     */
    @NotEmpty(message = "El nombre de usuario no puede estar vacío.")
    @Size(min = 3, max = 20, message = "El nombre de usuario debe tener entre 3 y 20 caracteres.")
    @Indexed(unique = true)
    private String username;

    /**
     * La contraseña del usuario, debe tener al menos 6 caracteres.
     */
    @NotEmpty(message = "La contraseña no puede estar vacía.")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    private String password;

    /**
     * El correo electrónico del usuario, debe ser único y válido.
     */
    @Email(message = "Debe ser una dirección de correo válida.")
    @NotEmpty(message = "El email no puede estar vacío.")
    @Indexed(unique = true)
    private String email;

    /**
     * La lista de medicamentos favoritos del usuario. Se carga de forma perezosa.
     */
    @DBRef(lazy = true)
    private List<Favorito> favoritos = new ArrayList<>();

    /**
     * El conjunto de roles asignados al usuario para el control de acceso.
     */
    private Set<String> roles = new HashSet<>();

    /**
     * Indica si la cuenta del usuario está habilitada.
     */
    private boolean enabled = true;
}