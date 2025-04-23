package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Usuario {
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
        System.out.print("Ingrese email: ");
        String correo = sc.nextLine();
        System.out.print("Ingrese contraseña: ");
        String clave = sc.nextLine();

        for (Usuario u : usuarios) {
            if (u.email.equals(correo) && u.contrasenia.equals(clave)) {
                u.sesionIniciada = true;
                System.out.println("Inicio de sesión exitoso. ¡Bienvenido, " + u.nombre + "!");
                return;
            }
        }
        System.out.println("Correo o contraseña incorrectos.");
    }
    public void registrarUsuario(){
        System.out.print("Ingrese su nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Ingrese su email: ");
        String correo = sc.nextLine();
        System.out.print("Cree una contraseña: ");
        String clave = sc.nextLine();

        // Validar si el usuario ya existe
        for (Usuario u : usuarios) {
            if (u.email.equals(correo)) {
                System.out.println("Este correo ya está registrado.");
                return;
            }
        }

        Usuario nuevo = new Usuario(nombre, correo, clave);
        usuarios.add(nuevo);
        System.out.println("Usuario registrado exitosamente.");
    }
    public void cambiarContrasenia(){
        System.out.print("Ingrese su email: ");
        String correo = sc.nextLine();
        System.out.print("Ingrese su contraseña actual: ");
        String clave = sc.nextLine();

        for (Usuario u : usuarios) {
            if (u.email.equals(correo) && u.contrasenia.equals(clave)) {
                System.out.print("Ingrese nueva contraseña: ");
                String nuevaClave = sc.nextLine();
                u.contrasenia = nuevaClave;
                System.out.println("Contraseña actualizada con éxito.");
                return;
            }
        }
        System.out.println("Credenciales incorrectas.");
    }
    public void cerrarSesion(){
        if (this.sesionIniciada) {
            this.sesionIniciada = false;
            System.out.println("Sesión cerrada con éxito.");
        } else {
            System.out.println("No hay sesión activa.");
        }
    }
    // Método auxiliar para pruebas (no obligatorio)
    public static void mostrarUsuariosRegistrados() {
        System.out.println("Usuarios registrados:");
        for (Usuario u : usuarios) {
            System.out.println("- " + u.nombre + " (" + u.email + ")");
        }
    }

    // Método para agregar un favorito a la lista del usuario
    public void agregarAFavoritos(Favorito favorito) {
        if (!favoritos.contains(favorito)) {
            favoritos.add(favorito);
            System.out.println("Agregado a favoritos: " + favorito.getMedicamento().getNombre());
        } else {
            System.out.println("El medicamento ya está en los favoritos.");
        }
    }

    // Método para eliminar un favorito de la lista del usuario
    public void eliminarDeFavoritos(Favorito favorito) {
        if (favoritos.contains(favorito)) {
            favoritos.remove(favorito);
            System.out.println("Eliminado de favoritos: " + favorito.getMedicamento().getNombre());
        } else {
            System.out.println("El medicamento no está en los favoritos.");
        }
    }

    // Método para mostrar todos los favoritos del usuario
    public void mostrarFavoritos() {
        System.out.println("Favoritos de " + nombre + ":");
        for (Favorito favorito : favoritos) {
            System.out.println(favorito.getMedicamento().getNombre());
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
