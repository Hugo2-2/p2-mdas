package com.GM2.controller;


import com.GM2.model.domain.Hijos;
import com.GM2.model.repository.HijosRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hijos")
public class HijosController {

    HijosRepository hijosRepository;

    public HijosController(HijosRepository hijosRepository) {
        this.hijosRepository = hijosRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.hijosRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping
    public List<Hijos> getHijos() { return hijosRepository.findAllHijos(); }

    @GetMapping("/{dni}")
    public Hijos getHijoByDni(@PathVariable String dni){ return hijosRepository.findHijoByDni(dni); }

    @PostMapping
    public String addHijo(@RequestBody Hijos hijo) {
        boolean res = hijosRepository.addHijo(hijo);

        if(res) {
            return "Hijo was added successfully";
        } else {
            return "Hijo could not be added";
        }
    }
}