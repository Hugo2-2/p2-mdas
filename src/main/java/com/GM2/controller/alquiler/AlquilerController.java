package com.GM2.controller.alquiler;

import com.GM2.model.domain.Acompanante;
import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Embarcacion;
import com.GM2.model.repository.AcompananteRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador web (MVC) para la gestión de Alquileres.
 * Maneja las peticiones web para mostrar formularios y procesar la
 * creación de nuevos alquileres.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
@Controller
@RequestMapping("/api/alquiler")
public class AlquilerController {

    AlquilerService alquilerService;
    AcompananteRepository acompanantesRepository;


    /**
     * Constructor para la inyección de dependencias.
     * Spring Boot inyectará automáticamente las instancias de los repositorios
     * y servicios necesarios.
     * 
     * @param alquilerService Servicio para la lógica de negocio de Alquileres.
     * @param acompanantesRepository Repositorio para el acceso a datos de Acompañantes.
     */
    public AlquilerController(AlquilerService alquilerService, AcompananteRepository acompanantesRepository) {
        this.alquilerService = alquilerService;    
        this.acompanantesRepository = acompanantesRepository;
    }


    /**
     * Muestra el formulario para agregar un nuevo alquiler.
     * 
     * @return ModelAndView con el formulario de alquiler
     */
    @GetMapping("/addAlquiler")
    public ModelAndView mostrarFormularioAlquiler() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addAlquilerView");
        modelAndView.addObject("alquiler", new Alquiler());
        return modelAndView; 
    }

    /**
     * Procesa el formulario para agregar un nuevo alquiler.
     * 
     * @param alquiler Objeto Alquiler con los datos del formulario
     * @param status Controlador de estado de la sesión para limpiarla tras el envío.
     * @return ModelAndView con el mensaje de éxito o error
     */
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

    /**
     * Muestra el formulario para gestionar acompañantes de un alquiler.
     * 
     * @param alquilerId ID del alquiler
     * @param plazas Número de plazas disponibles
     * @return ModelAndView con el formulario de acompañantes
     */
    @GetMapping("/acompanantes/{alquilerId}/{plazas}")
    public ModelAndView mostrarFormularioAcompanantes(@PathVariable Integer alquilerId, @PathVariable int plazas) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addAcompanante");

        // 1. Cargar acompañantes existentes de la base de datos
        List<Acompanante> acompanantesExistentes = acompanantesRepository.findAcompananteByAlquiler(alquilerId);
        
        // Si es null, crear lista vacía
        if (acompanantesExistentes == null) {
            acompanantesExistentes = new ArrayList<>();
        }

        // 2. Calcular cuántos acompañantes nuevos se pueden añadir
        int plazasDisponibles = plazas - 1 - acompanantesExistentes.size();
        
        // 3. Crear lista para nuevos acompañantes (solo los que caben)
        List<Acompanante> nuevosAcompanantes = new ArrayList<>();
        for (int i = 0; i < plazasDisponibles; i++) {
            nuevosAcompanantes.add(new Acompanante());
        }

        // 4. Pasar todos los datos al modelo
        modelAndView.addObject("alquilerId", alquilerId);
        modelAndView.addObject("plazas", plazas);
        modelAndView.addObject("acompanantesExistentes", acompanantesExistentes);
        modelAndView.addObject("acompanantes", nuevosAcompanantes); // Los nuevos que se pueden añadir
        modelAndView.addObject("plazasDisponibles", plazasDisponibles);

        return modelAndView;
    }


    /**
     * Obtiene todos los alquileres del sistema.
     * 
     * @return ModelAndView con la lista de alquileres
     */
    @GetMapping
    public ModelAndView getAlquileres() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("listAlquiler");
        
        List<Alquiler> alquileres = alquilerService.findAllAlquileres();
        modelAndView.addObject("alquileres", alquileres);
        
        return modelAndView;
    }

    /**
     * Obtiene los detalles de un alquiler específico.
     * 
     * @param id ID del alquiler
     * @return ModelAndView con los detalles del alquiler
     */
    @GetMapping("/{id}")
    public ModelAndView getAlquilerDetalles(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("detallesAlquiler");
        
        Alquiler alquiler = alquilerService.findAlquilerById(id);
        modelAndView.addObject("alquiler", alquiler);
        
        return modelAndView;
    }

    /**
     * Busca embarcaciones disponibles entre dos fechas.
     * 
     * @param inicio Fecha de inicio en formato String (opcional)
     * @param fin Fecha de fin en formato String (opcional)
     * @return ModelAndView con el formulario o resultados de búsqueda
     */
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
    

    /**
     * Obtiene todos los alquileres futuros.
     * 
     * @return ModelAndView con la lista de alquileres futuros
     */
    @GetMapping("/futuros")
    public ModelAndView getAlquileresFuturos() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("alquileresFuturos");
        
        List<Alquiler> alquileresFuturos = alquilerService.listarAlquileresFuturos();
        modelAndView.addObject("alquileresFuturos", alquileresFuturos);
        
        return modelAndView;
    }

}
