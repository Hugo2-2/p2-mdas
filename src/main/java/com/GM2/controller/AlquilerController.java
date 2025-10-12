package com.GM2.controller;

import com.GM2.model.domain.Alquiler;
import com.GM2.model.repository.AlquilerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alquiler")
public class AlquilerController {

    AlquilerRepository alquilerRepository;

    public AlquilerController(AlquilerRepository alquilerRepository) {
        this.alquilerRepository = alquilerRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.alquilerRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping
    public List<Alquiler> getAlquileres() { return  alquilerRepository.findAllAlquileres(); }

    @GetMapping("/{id}")
    public Alquiler getAlquilerById(@PathVariable Integer id) { return alquilerRepository.findAlquilerById(id); };

    @PostMapping
    public String addAlquiler(@RequestBody Alquiler alquiler) {
        boolean res = alquilerRepository.addAlquiler(alquiler);

        if(res) {
            return "Alquiler was added successfully";
        } else {
            return "Alquiler could not be added";
        }
    }

}
