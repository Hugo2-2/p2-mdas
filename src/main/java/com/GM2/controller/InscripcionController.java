package com.GM2.controller;

import com.GM2.model.domain.Inscripcion;
import com.GM2.model.repository.InscripcionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    private final InscripcionRepository inscripcionRepository;

    public InscripcionController(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }

    @GetMapping
    public List<Inscripcion> getInscripciones() {
        return inscripcionRepository.findAllInscripciones();
    }

    @GetMapping("/{id}")
    public Inscripcion getInscripcionById(@PathVariable String id) {
        return inscripcionRepository.findInscripcionById(id);
    }

    @PostMapping
    public String addInscripcion(@RequestBody Inscripcion inscripcion) {
        boolean res = inscripcionRepository.addInscripcion(inscripcion);
        if (res) {
            return "Inscripcion added successfully";
        } else {
            return "Error adding inscripcion";
        }
    }
}
