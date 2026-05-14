package com.GM2.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Reserva;
import com.GM2.model.repository.AlquilerRepository;
import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.ReservaRepository;

@RestController
@RequestMapping(path = "api/reservas", produces = "application/json")
public class ReservaRestController {
    private final ReservaRepository reservaRepository;
    private final EmbarcacionRepository embarcacionRepository;
    private final AlquilerRepository alquilerRepository;

    public ReservaRestController(ReservaRepository reservaRepository, EmbarcacionRepository embarcacionRepository, AlquilerRepository alquilerRepository) {
        this.reservaRepository = reservaRepository;
        this.embarcacionRepository = embarcacionRepository;
        this.alquilerRepository = alquilerRepository;
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> getAllReservas() {
        try {
            List<Reserva> reservas = reservaRepository.findAllReservas();
            return (reservas == null || reservas.isEmpty()) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(reservas, HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @GetMapping(params = "fecha")
    public ResponseEntity<List<Reserva>> getReservasFuturas(@RequestParam LocalDate fecha) {
        try {
            List<Reserva> todas = reservaRepository.findAllReservas();
            List<Reserva> futuras = new ArrayList<>();
            if (todas != null) { for (Reserva r : todas) { if (r.getDate().isAfter(fecha)) futuras.add(r); } }
            return futuras.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(futuras, HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable Integer id) {
        try {
            Reserva reserva = reservaRepository.findReservaById(id);
            return reserva == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(reserva, HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Reserva> createReserva(@RequestBody Reserva nuevaReserva) {
        try { return procesarCreacionReserva(nuevaReserva); }
        catch (Exception e) { return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    private ResponseEntity<Reserva> procesarCreacionReserva(Reserva nuevaReserva) {
        Embarcacion emb = embarcacionRepository.findEmbarcacionByMatricula(nuevaReserva.getBoatRegistration());
        if (emb == null) return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        if ((nuevaReserva.getSeats() + 1) > emb.getSeats()) return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        if (isEmbarcacionOcupada(nuevaReserva.getBoatRegistration(), nuevaReserva.getDate())) return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        nuevaReserva.setPrice(Reserva.PRICE_PER_SEAT * nuevaReserva.getSeats());
        boolean ok = reservaRepository.addReserva(nuevaReserva);
        return ok ? new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED) : new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PatchMapping("/{id}/fecha")
    public ResponseEntity<Reserva> updateReservaFecha(@PathVariable Integer id, @RequestBody Reserva datosNuevos) {
        try {
            Reserva existente = reservaRepository.findReservaById(id);
            if (existente == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            LocalDate nuevaFecha = datosNuevos.getDate();
            if (nuevaFecha == null || nuevaFecha.isBefore(LocalDate.now())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            if (isEmbarcacionOcupada(existente.getBoatRegistration(), nuevaFecha)) return new ResponseEntity<>(HttpStatus.CONFLICT);
            existente.setDate(nuevaFecha);
            boolean ok = reservaRepository.updateReserva(existente);
            return ok ? new ResponseEntity<>(existente, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) { return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PatchMapping("/{id}/detalles")
    public ResponseEntity<Reserva> updateReservaDetalles(@PathVariable Integer id, @RequestBody Reserva datosNuevos) {
        try {
            Reserva existente = reservaRepository.findReservaById(id);
            if (existente == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            if (datosNuevos.getDescription() != null) existente.setDescription(datosNuevos.getDescription());
            if (datosNuevos.getSeats() > 0) {
                Embarcacion emb = embarcacionRepository.findEmbarcacionByMatricula(existente.getBoatRegistration());
                if (emb != null && (datosNuevos.getSeats() + 1) > emb.getSeats()) return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
                existente.setSeats(datosNuevos.getSeats());
                existente.setPrice(Reserva.PRICE_PER_SEAT * datosNuevos.getSeats());
            }
            boolean ok = reservaRepository.updateReserva(existente);
            return ok ? new ResponseEntity<>(existente, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) { return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Integer id) {
        try {
            Reserva reserva = reservaRepository.findReservaById(id);
            if (reserva == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            if (reserva.getDate().isBefore(LocalDate.now())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            boolean ok = reservaRepository.deleteReserva(id);
            return ok ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) { return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    /**
     * Refactoring 1.4: Extract Method — se extraen tieneConflictoConAlquileres y tieneConflictoConReservas.
     */
    private boolean isEmbarcacionOcupada(String matricula, LocalDate fecha) {
        return tieneConflictoConAlquileres(matricula, fecha) || tieneConflictoConReservas(matricula, fecha);
    }

    private boolean tieneConflictoConAlquileres(String matricula, LocalDate fecha) {
        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        if (alquileres == null) return false;
        for (Alquiler a : alquileres) {
            if (a.getBoatRegistration().equals(matricula) && !fecha.isBefore(a.getStartDate()) && !fecha.isAfter(a.getEndDate()))
                return true;
        }
        return false;
    }

    private boolean tieneConflictoConReservas(String matricula, LocalDate fecha) {
        List<Reserva> reservas = reservaRepository.findAllReservas();
        if (reservas == null) return false;
        for (Reserva r : reservas) {
            if (r.getBoatRegistration().equals(matricula) && r.getDate().equals(fecha)) return true;
        }
        return false;
    }
}