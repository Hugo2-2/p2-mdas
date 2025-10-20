package com.GM2.controller;

import com.GM2.model.domain.Reserva;
import com.GM2.model.domain.Embarcacion;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/reserva")
public class ReservaController {

    ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;        
    }

    @GetMapping
    public List<Reserva> getReservas() { return  reservaService.findAllReservas(); }

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

    // POST /api/reserva/solicitar
    @PostMapping("/solicitar")
    public String solicitarReserva(@RequestBody Reserva reserva) {
        boolean res = reservaService.reservarEmbarcacion(reserva);
        if (res) {
            return "Reserva realizada con éxito";
        } else {
            return "No se pudo realizar la reserva (socio menor de edad, embarcación ocupada o insuficiente capacidad)";
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