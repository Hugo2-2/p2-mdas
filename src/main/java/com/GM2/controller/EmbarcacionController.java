package com.GM2.controller;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Patron;
import com.GM2.model.repository.EmbarcacionRepository;

import com.GM2.model.repository.PatronRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/embarcaciones")
public class EmbarcacionController {

    EmbarcacionRepository embarcacionRepository;
    AssignPatronToEmbarcacion assignPatronToEmbarcacion;

    public EmbarcacionController(EmbarcacionRepository embarcacionRepository, AssignPatronToEmbarcacion assignPatronToEmbarcacion) {
        this.embarcacionRepository = embarcacionRepository;
        this.assignPatronToEmbarcacion = assignPatronToEmbarcacion;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping
    public List<Embarcacion> getEmbarcaciones(){ return embarcacionRepository.findAllEmbarcaciones(); }

    @GetMapping("/{matricula}")
    public Embarcacion getEmbarcacionByMatricula(@PathVariable String matricula){ return embarcacionRepository.findEmbarcacionByMatricula(matricula); }

    @PostMapping
    public String addEmbarcacion(@RequestBody Embarcacion embarcacion){
        boolean res = embarcacionRepository.addEmbarcacion(embarcacion);

        if(res) {
            return "Embarcacion was added successfully";
        } else {
            return "Embarcacion could not be added";
        }
    }

    //TENGO QUE HACER AQUI LA DE OBTENER EMBARCACION POR TIPO


    @PutMapping("/{matricula}/patron")
    public String asociatePatronToEmbarcacion(@PathVariable String matricula, @RequestBody String patronDni){
        return assignPatronToEmbarcacion.asociatePatron(matricula, patronDni);
    }
}
