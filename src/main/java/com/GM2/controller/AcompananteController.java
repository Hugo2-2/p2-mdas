package com.GM2.controller;


import com.GM2.model.domain.Acompanante;
import com.GM2.model.repository.AcompananteRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador REST para gestionar las operaciones relacionadas con acompañantes.
 * Proporciona una API REST completa para crear, consultar y gestionar acompañantes
 * de alquileres mediante endpoints que devuelven datos en formato JSON.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
@RestController
@RequestMapping("/api/acompanantes")
public class AcompananteController {

    AcompananteRepository acompanantesRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param acompanantesRepository Repositorio de acompañantes para operaciones de datos
     */
    public AcompananteController(AcompananteRepository acompanantesRepository) {
        this.acompanantesRepository = acompanantesRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.acompanantesRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Obtiene todos los acompañantes registrados en el sistema.
     * 
     * @return Lista de todos los objetos {@link Acompanante} en formato JSON
     */
    @GetMapping
    public List<Acompanante> getAcompanantes() { return acompanantesRepository.findAllAcompanantes(); }

    /**
     * Obtiene un acompañante por su DNI.
     * 
     * @param dni DNI del acompañante a buscar
     * @return El objeto {@link Acompanante} encontrado o null si no existe.
     */
    @GetMapping("/{dni}")
    public Acompanante getAcompananteByDni(@PathVariable String dni){ return acompanantesRepository.findAcompananteByDni(dni); }

    /**
     * Obtiene todos los acompañantes de un alquiler.
     * 
     * @param id ID del alquiler
     * @return Lista de todos los objetos {@link Acompanante} de un alquiler en formato JSON
     */
    @GetMapping("/{id}")
    public List<Acompanante> getAcompanantesById(@PathVariable Integer id){ return acompanantesRepository.findAcompananteByAlquiler(id); }

    /**
     * Agrega acompañantes a un alquiler.
     * 
     * @param alquilerId ID del alquiler
     * @param dnis Lista de DNIs de los acompañantes
     * @param redirectAttributes Atributos para redirección y mensajes flash
     * @return String con la redirección a la vista de alquiler
     */
    @PostMapping
    public String addAcompanantes(@RequestParam("alquilerId") Integer alquilerId,
                      @RequestParam("dni") List<String> dnis,
                      RedirectAttributes redirectAttributes) {

        boolean ok = true;
        
        for (String dni : dnis) {
            if (dni != null && !dni.trim().isEmpty()) {
                Acompanante a = new Acompanante();
                a.setDni(dni);
                a.setId_alquiler(alquilerId);
                
                if (!acompanantesRepository.addAcompanante(a)) {
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