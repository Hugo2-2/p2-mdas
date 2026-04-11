package com.GM2.controller.embarcacion;

import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Reserva;
import com.GM2.model.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador encargado de gestionar la consulta de embarcaciones disponibles
 * en un rango de fechas concreto.
 *
 * Este controlador realiza las siguientes acciones:
 * - Carga desde los repositorios todas las embarcaciones, alquileres y reservas.
 * - Comprueba solapamientos entre el rango solicitado y las fechas existentes.
 * - Determina qué embarcaciones están libres para ser alquiladas.
 *
 * El resultado se devuelve en una vista que muestra un formulario y,
 * opcionalmente, la tabla con embarcaciones disponibles.
 */
@Controller
@RequestMapping("/api/embarcaciones")
public class GetAvailibleEmbarcacionesOnDateController {
    AlquilerRepository alquilerRepository;
    AcompananteRepository acompanantesRepository;
    ReservaRepository reservaRepository;
    SocioRepository socioRepository;
    EmbarcacionRepository embarcacionRepository;

    /**
     * Constructor para la inyección de dependencias.
     * Spring Boot inyectará automáticamente las instancias de los repositorios
     * y servicios necesarios.
     *
     * @param alquilerRepository Repositorio para operaciones de base de datos relacionadas con los alquileres.
     * @param acompanantesRepository Repositorio para el acceso a datos de Acompañantes.
     * @param reservaRepository Repositorio para el acceso a datos de Reservas.
     * @param socioRepository Repositorio para el acceso a datos de Socios.
     * @param embarcacionRepository Repositorio para el acceso a datos de Embarcaciones.
     */
    public GetAvailibleEmbarcacionesOnDateController(AlquilerRepository alquilerRepository, AcompananteRepository acompanantesRepository, ReservaRepository reservaRepository, SocioRepository socioRepository, EmbarcacionRepository embarcacionRepository) {
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
        modelAndView.setViewName("embarcacion/embarcacionDisponibleView");

        // Si no hay parámetros, mostrar solo el formulario
        if (inicio == null || fin == null) {
            return modelAndView;
        }

        // Si hay parámetros, procesar y mostrar resultados
        try {
            LocalDate fechaInicio = LocalDate.parse(inicio);
            LocalDate fechaFin = LocalDate.parse(fin);

            if (fechaInicio.isAfter(fechaFin)) {
                modelAndView.addObject("error", "La fecha de inicio no puede ser posterior a la de fin.");
                return modelAndView;
            }

            // Buscar embarcaciones disponibles entre dos fechas
            List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcaciones();
            List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();

            // Clean Code - Reglas de nombrado: el nombre de una coleccion ayuda a entender su contenido (disponibles -> availableBoats)
            List<Embarcacion> availableBoats = new ArrayList<>();
            List<Reserva> reservas = reservaRepository.findAllReservas();

            // Comprobar null
            if (embarcaciones == null) embarcaciones = new ArrayList<>();
            if (alquileres == null) alquileres = new ArrayList<>();
            if (reservas == null) reservas = new ArrayList<>();


            for (Embarcacion e : embarcaciones) {
                boolean ocupada = false;

                for (Alquiler a : alquileres) {
                    if (a.getMatricula_embarcacion().equals(e.getMatricula())) {
                        if (!(fechaFin.isBefore(a.getFechainicio()) || fechaInicio.isAfter(a.getFechafin()))) {
                            ocupada = true;
                            break;
                        }
                    }
                }

                if (!ocupada) {
                    for (Reserva r : reservas) {
                        if (r.getMatricula_embarcacion().equals(e.getMatricula())) {
                            // Una reserva ocupa la embarcación por UN DÍA específico
                            // Verificar si alguna fecha del rango de alquiler coincide con la fecha de reserva
                            LocalDate fechaReserva = r.getFecha();
                            if (!fechaReserva.isBefore(fechaInicio) && !fechaReserva.isAfter(fechaFin)) {
                                ocupada = true;
                                break;
                            }
                        }
                    }
                }

                if (!ocupada) {
                    availableBoats.add(e);
                }
            }
            modelAndView.addObject("disponibles", availableBoats);
            modelAndView.addObject("fechaInicio", fechaInicio);
            modelAndView.addObject("fechaFin", fechaFin);
            modelAndView.addObject("mostrarResultados", true);

        } catch (Exception e) {
            modelAndView.addObject("error", "Formato de fecha incorrecto");
        }

        return modelAndView;
    }
}
