package com.GM2.controller.reserva;

import com.GM2.model.domain.Reserva;
import com.GM2.model.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Controlador encargado de mostrar las reservas y sus detalles.
 * Estructura paralela a GetAlquilerController.
 */
@Controller
@RequestMapping("/api/reserva")
public class GetReservaController {

    ReservaRepository reservaRepository;
    EmbarcacionRepository embarcacionRepository;
    SocioRepository socioRepository;
    AlquilerRepository alquilerRepository;

    public GetReservaController(
            ReservaRepository reservaRepository,
            EmbarcacionRepository embarcacionRepository,
            SocioRepository socioRepository,
            AlquilerRepository alquilerRepository
    ) {
        this.reservaRepository = reservaRepository;
        this.embarcacionRepository = embarcacionRepository;
        this.socioRepository = socioRepository;
        this.alquilerRepository = alquilerRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.reservaRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.alquilerRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Muestra todas las reservas registradas.
     */
    @GetMapping
    public ModelAndView getReservas() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("listReservaView");

        List<Reserva> reservas = reservaRepository.findAllReservas();
        modelAndView.addObject("reservas", reservas);

        return modelAndView;
    }

    /**
     * Muestra los detalles de una reserva específica.
     */
    @GetMapping("/{id}")
    public ModelAndView getReservaDetalles(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("detallesReservaView");

        Reserva reserva = reservaRepository.findReservaById(id);
        modelAndView.addObject("reserva", reserva);

        return modelAndView;
    }
}
