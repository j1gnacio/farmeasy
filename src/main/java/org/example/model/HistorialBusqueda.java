package org.example.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "historial_busquedas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialBusqueda {
    @Id
    private String id = UUID.randomUUID().toString();

    @DBRef
    private Usuario usuario;

    private Date fecha = new Date();

    private String termino;

    private String ciudad;

    private String direccion;

    @DBRef
    private Farmacia farmacia;

    public HistorialBusqueda(Usuario usuario, String termino, String ciudad, String direccion, Farmacia farmacia) {
        this.usuario = usuario;
        this.termino = termino;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.farmacia = farmacia;
        this.fecha = new Date();
    }
}
