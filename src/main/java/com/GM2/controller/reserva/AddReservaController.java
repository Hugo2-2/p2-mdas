package com.GM2.controller.reserva;

import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Reserva;
import com.GM2.model.domain.Socio;
import com.GM2.model.repository.AlquilerRepository;
import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.ReservaRepository;
import com.GM2.model.repository.SocioRepository;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador web (MVC) para la gestión de Reservas.
 * Sigue la misma estructura que AddAlquilerController.
 */
@Controller
@RequestMapping("/api/reserva")
public class AddReservaController {

    ReservaRepository reservaRepository;
    EmbarcacionRepository embarcacionRepository;
    SocioRepository socioRepository;
    AlquilerRepository alquilerRepository;

    public AddReservaController(
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
     * Muestra el formulario para agregar una nueva reserva.
     */
    @GetMapping("/addReserva")
    public ModelAndView mostrarFormularioReserva() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addReservaView");
        modelAndView.addObject("reserva", new Reserva());
        return modelAndView;
    }

    /**
     * Procesa el formulario para agregar una nueva reserva.
     */
    @PostMapping("/addReserva")
    public ModelAndView procesarFormularioReserva(@ModelAttribute Reserva reserva, SessionStatus status) {
        ModelAndView modelAndView = new ModelAndView();
        String resultado = "";

        Socio socio = socioRepository.findSocioByDNI(reserva.getUsuario_id());

        // --- VALIDACIONES ---
        if (socio == null) {
            resultado = "El DNI del socio no está registrado.";
        } else if (!socio.esMayorEdad()) {
            resultado = "El socio debe ser mayor de edad para realizar una reserva.";
        } else {
            Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(reserva.getMatricula_embarcacion());
            if (embarcacion == null) {
                resultado = "La embarcación no existe.";
            } else if (embarcacion.getIdPatron() == null || embarcacion.getIdPatron().isEmpty()) {
                resultado = "La embarcación no tiene patrón asignado.";
            } else if (embarcacion.getPlazas() < reserva.getPlazas() + 1) {
                resultado = "Capacidad insuficiente (recuerda sumar 1 para el patrón).";
            } else {
                // --- DISPONIBILIDAD ---
                LocalDate fecha = reserva.getFecha();
                boolean disponible = true;

                List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
                List<Reserva> reservas = reservaRepository.findAllReservas();

                for (Alquiler a : alquileres) {
                    if (a.getMatricula_embarcacion().equals(embarcacion.getMatricula())
                            && !fecha.isBefore(a.getFechainicio())
                            && !fecha.isAfter(a.getFechafin())) {
                        disponible = false;
                        break;
                    }
                }

                for (Reserva r : reservas) {
                    if (r.getMatricula_embarcacion().equals(embarcacion.getMatricula())
                            && r.getFecha().equals(fecha)) {
                        disponible = false;
                        break;
                    }
                }

                if (!disponible) {
                    resultado = "La embarcación no está disponible en la fecha seleccionada.";
                } else {
                    // --- GUARDAR RESERVA ---
                    reserva.setPrecio(reserva.getPlazas() * 40.0);
                    boolean insertado = reservaRepository.addReserva(reserva);
                    resultado = insertado ? "EXITO" : "Error al guardar la reserva.";
                }
            }
        }

        if ("EXITO".equals(resultado)) {
            modelAndView.setViewName("redirect:/api/reserva");
        } else {
            modelAndView.setViewName("addReservaView");
            modelAndView.addObject("mensajeError", resultado);
        }

        status.setComplete();
        return modelAndView;
    }
}
