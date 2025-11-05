package com.GM2.controller;


import com.GM2.model.domain.Acompañantes;
import com.GM2.model.repository.AcompañantesRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/{id}")
    public List<Acompañantes> getAcompañanteById(@PathVariable Integer id){ return acompañantesRepository.findAcompañantesByAlquiler(id); }

    @PostMapping
    public String addAcompañantes(@RequestParam("alquilerId") Integer alquilerId,
                      @RequestParam("dni") List<String> dnis,
                      RedirectAttributes redirectAttributes) {

        boolean ok = true;
        
        for (String dni : dnis) {
            if (dni != null && !dni.trim().isEmpty()) {
                Acompañantes a = new Acompañantes();
                a.setDni(dni);
                a.setId_alquiler(alquilerId);
                
                if (!acompañantesRepository.addAcompañante(a)) {
                    ok = false;
                }
            }
        }

        if (ok) {
            redirectAttributes.addFlashAttribute("mensajeExito", 
                "✅ Acompañantes añadidos correctamente al alquiler " + alquilerId);
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", 
                "❌ Error al insertar algunos acompañantes.");
        }

        return "redirect:/api/alquiler";
    }

}