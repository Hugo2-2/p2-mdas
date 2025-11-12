package com.GM2.controller.alquiler;

import com.GM2.model.domain.Alquiler;
import com.GM2.model.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/api/alquiler")
public class GetAlquilerController {
    AlquilerRepository alquilerRepository;
    AcompananteRepository acompanantesRepository;
    ReservaRepository reservaRepository;
    SocioRepository socioRepository;
    EmbarcacionRepository embarcacionRepository;

    public GetAlquilerController(AlquilerRepository alquilerRepository, AcompananteRepository acompanantesRepository, ReservaRepository reservaRepository, SocioRepository socioRepository, EmbarcacionRepository embarcacionRepository) {
        this.alquilerRepository = alquilerRepository;
        this.acompanantesRepository = acompanantesRepository;
        this.reservaRepository = reservaRepository;
        this.socioRepository = socioRepository;
        this.embarcacionRepository = embarcacionRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.alquilerRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.acompanantesRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.reservaRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
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

        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
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

        Alquiler alquiler = alquilerRepository.findAlquilerById(id);
        modelAndView.addObject("alquiler", alquiler);

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

        List<Alquiler> alquileresFuturos = alquilerRepository.listarAlquileresFuturos();
        modelAndView.addObject("alquileresFuturos", alquileresFuturos);

        return modelAndView;
    }
}
