package com.GM2.controller;

import com.GM2.model.domain.Reserva;
import com.GM2.model.repository.ReservaRepository;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public List<Reserva> getReservas() { return  reservaRepository.findAllReservas(); }

    @GetMapping("/{id}")
    public Reserva getReservaById(@PathVariable Integer id) { return reservaRepository.findReservaById(id); };

    @PostMapping
    public String addReserva(@RequestBody Reserva reserva) {
        boolean res = reservaRepository.addReserva(reserva);

        if(res) {
            return "Reserva was added successfully";
        } else {
            return "Reserva could not be added";
        }
    }
    
}