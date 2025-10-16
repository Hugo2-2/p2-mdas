package com.GM2.controller;


import com.GM2.model.domain.Acompañantes;
import com.GM2.model.repository.AcompañantesRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acompanhantes")
public class AcompañantesController {

    AcompañantesRepository acompañantesRepository;

    public AcompañantesController(AcompañantesRepository acompañantesRepository) {
        this.acompañantesRepository = acompañantesRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.acompañantesRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping
    public List<Acompañantes> getAcompañantes() { return acompañantesRepository.findAllAcompañantes(); }

    @GetMapping("/{dni}")
    public Acompañantes getAcompañanteByDni(@PathVariable String dni){ return acompañantesRepository.findAcompañanteByDni(dni); }

    @PostMapping
    public String addAcompañante(@RequestBody Acompañantes acompañante) {
        boolean res = acompañantesRepository.addAcompañante(acompañante);

        if(res) {
            return "Acompañante was added successfully";
        } else {
            return "Acompañante could not be added";
        }
    }
}