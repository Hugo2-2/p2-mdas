package com.GM2.controller;

import com.GM2.model.domain.Socio;
import com.GM2.model.repository.SocioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// RestController no devuelve vistas, sino que retorna datos.
// Controller devuleve vistas, sin embargo se pueden obtener datos añadiendo @ResponseBody

//Por ahora voy a utilizar @ResController porque me interesa verificar que el backend funciona,
//no las vistas.

@RestController
@RequestMapping("/api/socios")
public class SocioController {

    SocioRepository socioRepository;

    public SocioController(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping
    public List<Socio> getSocios() {
        return socioRepository.findAllSocios();
    }

    @GetMapping("/{id}")
    public Socio getSocioById(@PathVariable int id) {
        return socioRepository.findSocioById(id);
    }

    @PostMapping
    public String addSocio(@RequestBody Socio socio) {
        boolean res = socioRepository.addSocio(socio);

        if(res) {
            return "Socio was added successfully";
        } else {
            return "Socio could not be added";
        }
    }
}
