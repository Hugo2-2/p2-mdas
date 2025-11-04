package com.GM2.controller.reserva;

import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Reserva;
import com.GM2.model.domain.Embarcacion;
import com.GM2.model.repository.ReservaRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/reserva")
public class ReservaController {

    private final ReservaRepository reservaRepository;
    ReservaService reservaService;

    public ReservaController(ReservaService reservaService, ReservaRepository reservaRepository) {
        this.reservaService = reservaService;
        this.reservaRepository = reservaRepository;
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


        String mensaje = reservaService.reservarEmbarcacion(reserva);
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
    public List<Reserva> getReservas(){ return reservaRepository.findAllReservas(); }


    @GetMapping("/{id}")
    public Reserva getReservaById(@PathVariable Integer id) { return reservaService.findReservaById(id); };

    @PostMapping
    public String addReserva(@RequestBody Reserva reserva) {
        boolean res = reservaService.addReserva(reserva);

        if(res) {
            return "Reserva was added successfully";
        } else {
            return "Reserva could not be added";
        }
    }
    
    // GET /api/reserva/disponibles?fecha=2025-10-25&plazas=5
    @GetMapping("/disponibles")
    public List<Embarcacion> getEmbarcacionesDisponibles(
            @RequestParam("fecha") String fecha,
            @RequestParam("plazas") int plazas) {

        LocalDate fechaReserva = LocalDate.parse(fecha);
        return reservaService.buscarEmbarcacionesConPatronDisponibles(fechaReserva, plazas);
    }

}