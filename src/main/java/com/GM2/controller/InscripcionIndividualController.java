package com.GM2.controller;

import com.GM2.model.domain.InscripcionIndividual;
import com.GM2.model.repository.InscripcionIndividualRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionIndividualController {

    private final InscripcionIndividualRepository inscripcionRepository;

    public InscripcionIndividualController(InscripcionIndividualRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }

    @GetMapping
    public List<InscripcionIndividual> getInscripciones() {
        return inscripcionRepository.findAllInscripciones();
    }

    @GetMapping("/{id}")
    public InscripcionIndividual getInscripcionById(@PathVariable String id) {
        return inscripcionRepository.findInscripcionById(id);
    }

    @PostMapping
    public String addInscripcion(@RequestBody InscripcionIndividual inscripcion) {
        boolean res = inscripcionRepository.addInscripcion(inscripcion);
        if (res) {
            return "Inscripcion added successfully";
        } else {
            return "Error adding inscripcion";
        }
    }
}
