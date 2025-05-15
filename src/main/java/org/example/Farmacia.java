package org.example;

import java.util.ArrayList;
import java.util.List;

public class Farmacia {
    private String nombre;
    private List<Medicamento> listaMedicamentos = new ArrayList<>();

    public Farmacia(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Medicamento> getListaMedicamentos() {
        return listaMedicamentos;
    }

    public void agregarMedicamento(Medicamento medicamento) {
        listaMedicamentos.add(medicamento);
    }
}
