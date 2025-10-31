package com.GM2.controller;

import com.GM2.model.domain.Acompañantes;
import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Embarcacion;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
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

        if (resultado.startsWith("OK:") ){

        
            // Extraer el id del alquiler
            Integer alquilerId = Integer.parseInt(resultado.substring(3));
            int plazas = alquiler.getPlazas();

        modelAndView.setViewName("redirect:/api/alquiler/acompanantes/" + alquilerId + "/" + plazas);
    
        } else {
            // Fallo: volvemos al formulario y mostramos mensaje de error
            modelAndView.setViewName("addAlquilerView");
            modelAndView.addObject("mensajeError", resultado);
        }
        status.setComplete();

        return modelAndView;
    }

    @GetMapping("/acompanantes/{alquilerId}/{plazas}")
    public ModelAndView mostrarFormularioAcompanantes(@PathVariable Integer alquilerId, @PathVariable int plazas) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addAcompanante");

        // Creamos una lista vacía de acompañantes según el número de plazas - 1
        List<Acompañantes> acompanantes = new ArrayList<>();
        for (int i = 0; i < plazas - 1; i++) {
            acompanantes.add(new Acompañantes());
        }

        modelAndView.addObject("alquilerId", alquilerId);
        modelAndView.addObject("acompanantes", acompanantes);


        return modelAndView;
    }


    @GetMapping
    public ModelAndView getAlquileres() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("listAlquiler");
        
        List<Alquiler> alquileres = alquilerService.findAllAlquileres();
        modelAndView.addObject("alquileres", alquileres);
        
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getAlquilerDetalles(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("detallesAlquiler");
        
        Alquiler alquiler = alquilerService.findAlquilerById(id);
        modelAndView.addObject("alquiler", alquiler);
        
        return modelAndView;
    }

    // Buscar embarcaciones disponibles entre dos fechas
    @GetMapping("/disponibles")
    public ModelAndView getEmbarcacionesDisponibles(
        @RequestParam(value = "inicio", required = false) String inicio,
        @RequestParam(value = "fin", required = false) String fin) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("alquilerDisponible");
        
        // Si no hay parámetros, mostrar solo el formulario
        if (inicio == null || fin == null) {
            return modelAndView;
        }
        
        // Si hay parámetros, procesar y mostrar resultados
        try {
            LocalDate fechaInicio = LocalDate.parse(inicio);
            LocalDate fechaFin = LocalDate.parse(fin);
            
            List<Embarcacion> disponibles = alquilerService.buscarEmbarcacionesDisponibles(fechaInicio, fechaFin);
            
            modelAndView.addObject("disponibles", disponibles);
            modelAndView.addObject("fechaInicio", fechaInicio);
            modelAndView.addObject("fechaFin", fechaFin);
            modelAndView.addObject("mostrarResultados", true);
            
        } catch (Exception e) {
            modelAndView.addObject("error", "Formato de fecha incorrecto");
        }
        
        return modelAndView;
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
    public ModelAndView getAlquileresFuturos() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("alquileresFuturos");
        
        List<Alquiler> alquileresFuturos = alquilerService.listarAlquileresFuturos();
        modelAndView.addObject("alquileresFuturos", alquileresFuturos);
        
        return modelAndView;
    }

}
