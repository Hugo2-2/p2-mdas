package com.GM2.controller;

import com.GM2.model.domain.InscripcionFamiliar;
import com.GM2.model.repository.InscripcionFamiliarRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscripcionesFamiliares")
public class InscripcionFamiliarController {

    private final InscripcionFamiliarRepository inscripcionRepository;

    public InscripcionFamiliarController(InscripcionFamiliarRepository inscripcionFamiliarRepository) {
        this.inscripcionRepository = inscripcionFamiliarRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.inscripcionRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping
    public List<InscripcionFamiliar> findAll() {
        return inscripcionRepository.findAllInscripciones();
    }

    @GetMapping("/{id}")
    public InscripcionFamiliar findById(@PathVariable int id) {
        return inscripcionRepository.findInscripcionFamiliarById(id);
    }

    @PostMapping
    public String addInscripcion(@RequestBody InscripcionFamiliar inscripcionFamiliar) {
        boolean res = inscripcionRepository.addInscripcionFamiliar(inscripcionFamiliar);

        if(res)
            return "Inscripcion Familiar added";
        else
            return "Error adding Inscripcion Familiar";
    }
}
