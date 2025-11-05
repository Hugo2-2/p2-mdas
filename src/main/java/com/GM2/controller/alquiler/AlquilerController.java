package com.GM2.controller.alquiler;

import com.GM2.model.domain.Acompañantes;
import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Embarcacion;
import com.GM2.model.repository.AcompañantesRepository;
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
    AcompañantesRepository acompanantesRepository;

    public AlquilerController(AlquilerService alquilerService, AcompañantesRepository acompanantesRepository) {
        this.alquilerService = alquilerService;    
        this.acompanantesRepository = acompanantesRepository;
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

        // 1. Cargar acompañantes existentes de la base de datos
        List<Acompañantes> acompanantesExistentes = acompanantesRepository.findAcompañantesByAlquiler(alquilerId);
        
        // Si es null, crear lista vacía
        if (acompanantesExistentes == null) {
            acompanantesExistentes = new ArrayList<>();
        }

        // 2. Calcular cuántos acompañantes nuevos se pueden añadir
        int plazasDisponibles = plazas - 1 - acompanantesExistentes.size();
        
        // 3. Crear lista para nuevos acompañantes (solo los que caben)
        List<Acompañantes> nuevosAcompanantes = new ArrayList<>();
        for (int i = 0; i < plazasDisponibles; i++) {
            nuevosAcompanantes.add(new Acompañantes());
        }

        // 4. Pasar todos los datos al modelo
        modelAndView.addObject("alquilerId", alquilerId);
        modelAndView.addObject("plazas", plazas);
        modelAndView.addObject("acompanantesExistentes", acompanantesExistentes);
        modelAndView.addObject("acompanantes", nuevosAcompanantes); // Los nuevos que se pueden añadir
        modelAndView.addObject("plazasDisponibles", plazasDisponibles);

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
