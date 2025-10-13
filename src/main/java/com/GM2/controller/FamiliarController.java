package com.GM2.controller;

import com.GM2.model.domain.Familiar;
import com.GM2.model.repository.FamiliarRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
 
@RestController
@RequestMapping("/api/familiar")
public class FamiliarController {
    FamiliarRepository familiarRepository;

    public FamiliarController(FamiliarRepository familiarRepository) {
        this.familiarRepository = familiarRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.familiarRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping
    public List<Familiar> getFamiliares() {
        return  familiarRepository.findAllFamiliares();
    }

    @GetMapping("/{id}")
    public Familiar getFamiliarById(@PathVariable String id) {
        return familiarRepository.findFamiliarById(id);
    }

    @PostMapping
    public String addFamiliar(@RequestBody Familiar familiar) {
        boolean res = familiarRepository.addFamiliar(familiar);
        if (res) {
            return "Familiar added successfully";
        } else {
            return "Error adding familiar";
        }
    }
}
