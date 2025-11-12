package com.GM2.controller.reserva;

import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Reserva;
import com.GM2.model.domain.Embarcacion;
import com.GM2.model.repository.ReservaRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/reserva")
public class ReservaController {

    ReservaRepository reservaRepository;


    public ReservaController(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.reservaRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }


    // Mostrar formulario de nueva reserva
    @GetMapping("/addReserva")
    public ModelAndView mostrarFormularioReserva() {
        ModelAndView modelAndView = new ModelAndView("addReservaView");
        modelAndView.addObject("reserva", new Reserva());
        return modelAndView;
    }

    // Procesar formulario de reserva
    @PostMapping("/addReserva")
    public ModelAndView procesarFormularioReserva(@ModelAttribute Reserva reserva,
            SessionStatus status,RedirectAttributes redirectAttributes) {

        //Mensajes para depurar en terminal
        System.out.println("[ReservaController] Informacion recivida: fecha=" + reserva.getFecha() +
                " plazas=" + reserva.getPlazas() +
                " Precio=" + reserva.getPrecio() +
                " Usuario_id=" + reserva.getUsuario_id() +
                " matricula_embarcación=" + reserva.getMatricula_embarcacion() +
                " descripción=" + reserva.getDescripcion());


        String mensaje = reservaRepository.reservarEmbarcacion(reserva);
        status.setComplete();

        if(mensaje.equals("EXITO")) {
            redirectAttributes.addFlashAttribute("mensaje", "Reserva realizada con exito.");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", mensaje);
        }

        return new ModelAndView("redirect:/api/reserva/addReserva");
    }


    // En ReservaController.java
    @GetMapping
    public ModelAndView getReserva() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("listReservaView");

        List<Reserva> reservas = reservaRepository.findAllReservas();
        modelAndView.addObject("reservas", reservas);

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getReservasDetalles(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("detallesReservaView");

        Reserva reserva = reservaRepository.findReservaById(id);
        modelAndView.addObject("reserva", reserva);

        return modelAndView;
    }
    // GET /api/reserva/disponibles?fecha=2025-10-25&plazas=5
    @GetMapping("/disponibles")
    public List<Embarcacion> getEmbarcacionesDisponibles(
            @RequestParam("fecha") String fecha,
            @RequestParam("plazas") int plazas) {

        LocalDate fechaReserva = LocalDate.parse(fecha);
        return reservaRepository.buscarEmbarcacionesConPatronDisponibles(fechaReserva, plazas);
    }

}