package com.GM2.controller.alquiler;

import com.GM2.model.domain.Alquiler;
import com.GM2.model.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador web (MVC) para la gestión de Alquileres.
 * Maneja las peticiones web para mostrar formularios referentes a los alquileres.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
@Controller
@RequestMapping("/api/alquiler")
public class GetAlquilerController {

    AlquilerRepository alquilerRepository;

    /**
     * Constructor para la inyección de dependencias.
     * Spring Boot inyectará automáticamente las instancias de los repositorios
     * y servicios necesarios.
     * 
     * @param alquilerRepository Repositorio para operaciones de base de datos relacionadas con los alquileres.
     */
    public GetAlquilerController(AlquilerRepository alquilerRepository) {
        this.alquilerRepository = alquilerRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.alquilerRepository.setSqlQueriesFileName(sqlQueriesFileName);
        
    }


    /**
     * Obtiene todos los alquileres del sistema.
     *
     * @return ModelAndView con la lista de objetos {@link Alquiler}
     */
    @GetMapping
    public ModelAndView getAlquileres() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("listAlquiler");

        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        modelAndView.addObject("alquileres", alquileres);

        return modelAndView;
    }

    /**
     * Obtiene los detalles de un alquiler específico.
     *
     * @param id ID del alquiler
     * @return ModelAndView con los detalles del objeto {@link Alquiler}
     */
    @GetMapping("/{id}")
    public ModelAndView getAlquilerDetalles(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("detallesAlquiler");

        Alquiler alquiler = alquilerRepository.findAlquilerById(id);
        modelAndView.addObject("alquiler", alquiler);

        return modelAndView;
    }

    /**
     * Obtiene todos los alquileres futuros.
     *
     * @return ModelAndView con la lista de objetos {@link Alquiler} futuros
     */
    @GetMapping("/futuros")
    public ModelAndView getAlquileresFuturos() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("alquileresFuturos");

        LocalDate hoy = LocalDate.now();
        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        List<Alquiler> futuros = new ArrayList<>();

        for (Alquiler a : alquileres) {

            if (!a.getFechainicio().isBefore(hoy)){ 
                futuros.add(a);
            }

        }

        modelAndView.addObject("alquileresFuturos", futuros);

        return modelAndView;
    }
}
