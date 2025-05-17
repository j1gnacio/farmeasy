package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // Importar para generar un ID único
import java.util.logging.Logger;
import java.util.logging.Level;

public class Medicamento {
    private static final Logger logger = Logger.getLogger(Medicamento.class.getName());

    private String idMedicamento;
    private String nombre;
    private String dosis;
    private String descripcion;
    private List<PrecioFarmacia> precios = new ArrayList<>();

    // Constructor simple: para cuando solo conocemos nombre y dosis
    public Medicamento(String nombre, String dosis) {
        this.idMedicamento = UUID.randomUUID().toString(); // Generar un ID único
        this.nombre = nombre;
        this.dosis = dosis;
    }

    public Medicamento(String nombre) {
        this.idMedicamento = UUID.randomUUID().toString(); // Generar un ID único
        this.nombre = nombre;
    }

    // Constructor completo: útil si tienes más información disponible
    public Medicamento(String idMedicamento, String nombre, String dosis, String descripcion) {
        this.idMedicamento = idMedicamento; // Permitir establecer un ID específico
        this.nombre = nombre;
        this.dosis = dosis;
        this.descripcion = descripcion;
    }

    public void obtenerDetalles() {
        logger.info("Medicamento ID: " + idMedicamento);
        logger.info("Medicamento: " + nombre + " " + (dosis != null ? dosis : ""));
        if (descripcion != null) {
            logger.info("Descripción: " + descripcion);
        }

        if (precios.isEmpty()) {
            logger.info("No hay precios disponibles aún.");
        } else {
            for (PrecioFarmacia precio : precios) {
                precio.obtenerPrecio(); // Llama al método que muestra precios y farmacia
            }
        }
    }

    public void agregarPrecio(PrecioFarmacia precio) {
        precios.add(precio);
    }

    public void verificar() {
        logger.info("Verificando disponibilidad y precios de " + nombre + "...");
        // Aquí podrías agregar lógica como: revisar si hay ofertas o si hay stock
    }

    // Getters
    public String getIdMedicamento() {
        return idMedicamento;
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

    // Setter para idMedicamento, en caso de que necesites establecerlo después
    public void setIdMedicamento(String idMedicamento) {
        this.idMedicamento = idMedicamento;
    }
}
