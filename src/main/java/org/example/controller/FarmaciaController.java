package org.example.controller;

import org.example.model.Farmacia;
import org.example.service.FarmaciaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmacias")
public class FarmaciaController {

    private final FarmaciaService service;

    public FarmaciaController(FarmaciaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Farmacia> obtenerTodas() {
        return service.obtenerTodas();
    }

    @PostMapping
    public Farmacia guardarFarmacia(@RequestBody Farmacia farmacia) {
        return service.guardar(farmacia);
    }

    @GetMapping("/buscar")
    public List<Farmacia> buscarPorNombre(@RequestParam String nombre) {
        return service.buscarPorNombre(nombre);
    }
}
