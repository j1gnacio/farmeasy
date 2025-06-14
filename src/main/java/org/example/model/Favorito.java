package org.example.model;

public class Favorito {
    private String idFavorito;
    private Usuario usuario;
    private Medicamento medicamento;

    public Favorito(Usuario usuario, Medicamento medicamento) {
        this.usuario = usuario;
        this.medicamento = medicamento;
        this.idFavorito = generarIdUnico(); // Genera un ID único para el favorito
    }

    private String generarIdUnico() {
        return "FAV-" + System.currentTimeMillis(); // ID único basado en la hora
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getIdFavorito() {
        return idFavorito;
    }
}
