package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;;

public class Usuario {
    private static final Logger logger = Logger.getLogger(Usuario.class.getName());
    private static final Scanner sc = new Scanner(System.in);
    private static final List<Usuario> usuarios = new ArrayList<>(); // "Base de datos" en memoria


    private String id;
    private String nombre;
    private String email;
    private String contrasenia;
    
    private boolean sesionIniciada = false;
    private List<Favorito> favoritos;
    private List<AlertaPrecio> alertas;

    public Usuario(String nombre, String email, String contrasenia){
        this.nombre=nombre;
        this.email=email;
        this.contrasenia=contrasenia;
        this.favoritos = new ArrayList<>();
        this.alertas=new ArrayList<>();
    }
    public Usuario(String nombre){
        this.nombre=nombre;
        this.favoritos = new ArrayList<>();
        this.alertas=new ArrayList<>();
    }

    public void iniciarSesion(){
        logger.info("Ingrese email: ");
        String correo = sc.nextLine();
        logger.info("Ingrese contraseña: ");
        String clave = sc.nextLine();

        for (Usuario u : usuarios) {
            if (u.email.equals(correo) && u.contrasenia.equals(clave)) {
                u.sesionIniciada = true;
                logger.info("Inicio de sesión exitoso. ¡Bienvenido, " + u.nombre + "!");
                return;
            }
        }
        logger.info("Correo o contraseña incorrectos.");
    }
    public void registrarUsuario(){
        logger.info("Ingrese su nombre: ");
        String nombre = sc.nextLine();
        logger.info("Ingrese su email: ");
        String correo = sc.nextLine();
        logger.info("Cree una contraseña: ");
        String clave = sc.nextLine();

        // Validar si el usuario ya existe
        for (Usuario u : usuarios) {
            if (u.email.equals(correo)) {
                logger.info("Este correo ya está registrado.");
                return;
            }
        }

        Usuario nuevo = new Usuario(nombre, correo, clave);
        usuarios.add(nuevo);
        logger.info("Usuario registrado exitosamente.");
    }
    public void cambiarContrasenia(){
        logger.info("Ingrese su email: ");
        String correo = sc.nextLine();
        logger.info("Ingrese su contraseña actual: ");
        String clave = sc.nextLine();

        for (Usuario u : usuarios) {
            if (u.email.equals(correo) && u.contrasenia.equals(clave)) {
                logger.info("Ingrese nueva contraseña: ");
                String nuevaClave = sc.nextLine();
                u.contrasenia = nuevaClave;
                logger.info("Contraseña actualizada con éxito.");
                return;
            }
        }
        logger.info("Credenciales incorrectas.");
    }
    public void cerrarSesion(){
        if (this.sesionIniciada) {
            this.sesionIniciada = false;
            logger.info("Sesión cerrada con éxito.");
        } else {
            logger.info("No hay sesión activa.");
        }
    }
    // Método auxiliar para pruebas (no obligatorio)
    public static void mostrarUsuariosRegistrados() {
        logger.info("Usuarios registrados:");
        for (Usuario u : usuarios) {
            logger.info("- " + u.nombre + " (" + u.email + ")");
        }
    }

    // Método para agregar un favorito a la lista del usuario
    public void agregarAFavoritos(Favorito favorito) {
        if (!favoritos.contains(favorito)) {
            favoritos.add(favorito);
            logger.info("Agregado a favoritos: " + favorito.getMedicamento().getNombre());
        } else {
            logger.info("El medicamento ya está en los favoritos.");
        }
    }

    // Método para eliminar un favorito de la lista del usuario
    public void eliminarDeFavoritos(Favorito favorito) {
        if (favoritos.contains(favorito)) {
            favoritos.remove(favorito);
            logger.info("Eliminado de favoritos: " + favorito.getMedicamento().getNombre());
        } else {
            logger.info("El medicamento no está en los favoritos.");
        }
    }

    // Método para mostrar todos los favoritos del usuario
    public void mostrarFavoritos() {
        logger.info("Favoritos de " + nombre + ":");
        for (Favorito favorito : favoritos) {
            logger.info(favorito.getMedicamento().getNombre());
        }
    }
    public void agregarAlerta(AlertaPrecio alerta) {
        alertas.add(alerta);
    }
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public boolean isSesionIniciada() {
        return sesionIniciada;
    }
    public List<Favorito> getFavoritos() {
        return favoritos;
    }
}
