package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Farmacia {
    private static final Logger logger = Logger.getLogger(Farmacia.class.getName());

    private static int contador = 0; // Cambiado a static
    private int idFarmacia;
    private String nombre;
    private Ubicacion ubicacion;
    private List<Medicamento> listaMedicamentos = new ArrayList<>(); // ✅ INICIALIZADA
    private List<PrecioFarmacia> precios = new ArrayList<>();

    public Farmacia(String nombre, Ubicacion ubicacion) {
        contador++; // Incrementar el contador
        this.idFarmacia = contador; // Asignar el idFarmacia
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public void obtenerMedicamentos() {
        logger.info("Obteniendo medicamentos de la página web de la farmacia: " + nombre);

        // Simulamos que se extrajeron estos medicamentos
        listaMedicamentos.add(new Medicamento("Paracetamol", "500mg"));
        listaMedicamentos.add(new Medicamento("Ibuprofeno", "400mg"));
        listaMedicamentos.add(new Medicamento("Amoxicilina", "500mg"));

        logger.info("Medicamentos obtenidos correctamente.");
    }

    public List<Medicamento> getListaMedicamentos() {
        return listaMedicamentos;
    }

    public int getIdFarmacia(){
        return this.idFarmacia;
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
