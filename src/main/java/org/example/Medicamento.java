package org.example;

import java.util.ArrayList;
import java.util.List;

public class Medicamento {
    private String idMedicamento;
    private String nombre;
    private String dosis;
    private String descripcion;
    private List<PrecioFarmacia> precios = new ArrayList<>();

    public void obtenerDetalles() {
        System.out.println("Medicamento: " + nombre + " " + (dosis != null ? dosis : ""));
        if (descripcion != null) {
            System.out.println("Descripción: " + descripcion);
        }

        if (precios.isEmpty()) {
            System.out.println("No hay precios disponibles aún.");
        } else {
            for (PrecioFarmacia precio : precios) {
                precio.obtenerPrecio(); // Llama al método que muestra precios y farmacia
            }
        }
    }

    // Constructor simple: para cuando solo conocemos nombre y dosis
    public Medicamento(String nombre, String dosis) {
        this.nombre = nombre;
        this.dosis = dosis;
    }
    public Medicamento(String nombre){
        this.nombre=nombre;
    }

    // Constructor completo: útil si tienes más información disponible
    public Medicamento(String idMedicamento, String nombre, String dosis, String descripcion) {
        this.idMedicamento = idMedicamento;
        this.nombre = nombre;
        this.dosis = dosis;
        this.descripcion = descripcion;
    }



    public void agregarPrecio(PrecioFarmacia precio) {
        precios.add(precio);
    }



    public void verificar() {
        System.out.println("Verificando disponibilidad y precios de " + nombre + "...");
        // Aquí podrías agregar lógica como: revisar si hay ofertas o si hay stock
    }

    public String getNombre() {
        return nombre;
    }

    public String getDosis() {
        return dosis;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
