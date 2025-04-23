package org.example;

import java.util.ArrayList;
import java.util.List;

public class Farmacia {
    private String idFarmacia;
    private String nombre;
    private Ubicacion ubicacion;
    private List<Medicamento> listaMedicamentos = new ArrayList<>(); // ✅ INICIALIZADA
    private List<PrecioFarmacia> precios = new ArrayList<>();

    public Farmacia(String nombre, Ubicacion ubicacion){
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public void obtenerMedicamentos(){

        System.out.println("Obteniendo medicamentos de la página web de la farmacia: " + nombre);

        // Simulamos que se extrajeron estos medicamentos
        listaMedicamentos.add(new Medicamento("Paracetamol", "500mg"));
        listaMedicamentos.add(new Medicamento("Ibuprofeno", "400mg"));
        listaMedicamentos.add(new Medicamento("Amoxicilina", "500mg"));

        System.out.println("Medicamentos obtenidos correctamente.");
    }

    public List<Medicamento> getListaMedicamentos() {
        return listaMedicamentos;
    }

    public String getNombre() {
        return nombre;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public List<PrecioFarmacia> getPrecios() {
        return precios;
    }

    public void agregarPrecio(PrecioFarmacia precio) {
        precios.add(precio);
    }
}
