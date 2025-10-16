package com.GM2.controller;


import com.GM2.model.domain.Patron;
import com.GM2.model.domain.Socio;
import com.GM2.model.repository.PatronRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrones")
public class PatronController {

    PatronRepository patronRepository;

    public PatronController(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping
    public List<Patron> getPatrones() { return patronRepository.findAllPatrones(); }

    @GetMapping("/{dni}")
    public Patron getPatronById(@PathVariable String dni){ return patronRepository.findPatronByDNI(dni); }

    @PostMapping
    public String addPatron(@RequestBody Patron patron) {
        boolean res = patronRepository.addPatron(patron);

        if(res) {
            return "Patron was added successfully";
        } else {
            return "Patron could not be added";
        }
    }
}
