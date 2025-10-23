package com.GM2.controller;

import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Embarcacion;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/alquiler")
public class AlquilerController {

    AlquilerService alquilerService;

    public AlquilerController(AlquilerService alquilerService) {
        this.alquilerService = alquilerService;        
    }


    @GetMapping("/addAlquiler")
    public ModelAndView mostrarFormularioAlquiler() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addAlquilerView");
        modelAndView.addObject("alquiler", new Alquiler());
        return modelAndView; 
    }

    @PostMapping("/addAlquiler")
    public ModelAndView procesarFormularioAlquiler(@ModelAttribute Alquiler alquiler, SessionStatus status) {
    
        ModelAndView modelAndView = new ModelAndView();

        String resultado = alquilerService.alquilarEmbarcacion(alquiler);
        status.setComplete();

        if (resultado.startsWith("Alquiler registrado con éxito.") ){

        
            // Éxito: volvemos al formulario, podemos añadir mensaje de éxito
            modelAndView.setViewName("addAlquilerView");
            modelAndView.addObject("mensajeExito", resultado);
        } else {
            // Fallo: volvemos al formulario y mostramos mensaje de error
            modelAndView.setViewName("addAlquilerView");
            modelAndView.addObject("mensajeError", resultado);
        }
        return modelAndView;
    }

    @GetMapping
    public List<Alquiler> getAlquileres() { return  alquilerService.findAllAlquileres(); }

    @GetMapping("/{id}")
    public Alquiler getAlquilerById(@PathVariable Integer id) { return alquilerService.findAlquilerById(id); };

    @PostMapping
    public String addAlquiler(@RequestBody Alquiler alquiler) {
        boolean res = alquilerService.addAlquiler(alquiler);

        if(res) {
            return "Alquiler was added successfully";
        } else {
            return "Alquiler could not be added";
        }
    }

    // Buscar embarcaciones disponibles entre dos fechas
    // Ejemplo: GET /api/alquiler/disponibles?inicio=2025-10-20&fin=2025-10-25
    @GetMapping("/disponibles")
    public List<Embarcacion> getEmbarcacionesDisponibles(
        @RequestParam("inicio") String inicio,
        @RequestParam("fin") String fin) {

            LocalDate fechaInicio = LocalDate.parse(inicio);
            LocalDate fechaFin = LocalDate.parse(fin);

            return alquilerService.buscarEmbarcacionesDisponibles(fechaInicio, fechaFin);
        }
    
    // Registrar un nuevo alquiler (alquilar embarcación)
    // Ejemplo: POST /api/alquiler/alquilar
    @PostMapping("/alquilar")
    public String alquilarEmbarcacion(@RequestBody Alquiler alquiler) {
        return alquilerService.alquilarEmbarcacion(alquiler);

    }

    // Listar solo los alquileres futuros
    // Ejemplo: GET /api/alquiler/futuros
    @GetMapping("/futuros")
    public List<Alquiler> getAlquileresFuturos() {
        return alquilerService.listarAlquileresFuturos();
    }

}
