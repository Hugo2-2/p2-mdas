package com.GM2.controller.alquiler;

import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Reserva;
import com.GM2.model.domain.Socio;
import com.GM2.model.repository.AlquilerRepository;
import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.ReservaRepository;
import com.GM2.model.repository.SocioRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador web (MVC) para la gestión de Alquileres.
 * Maneja las peticiones web para procesar la
 * creación de nuevos alquileres.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
@Controller
@RequestMapping("/api/alquiler")
public class AddAlquilerController {

    AlquilerRepository alquilerRepository;
    ReservaRepository reservaRepository;
    SocioRepository socioRepository;
    EmbarcacionRepository embarcacionRepository;

    // Constantes para estaciones y limites de alquiler
    // Clean Code - Regla 7: Extracción de valores numéricos constantes a constantes descriptivas (evitar números mágicos).
    private static final int MES_INICIO_TEMPORADA_BAJA_OCT = 10;
    private static final int MES_FIN_TEMPORADA_BAJA_ABR = 4;
    private static final int MAX_DIAS_ALQUILER_TEMPORADA_BAJA = 3;
    private static final int MES_INICIO_TEMPORADA_ALTA_MAY = 5;
    private static final int MES_FIN_TEMPORADA_ALTA_SEP = 9;
    private static final int SEMANA_TEMPORADA_ALTA = 7;
    private static final int QUINCENA_TEMPORADA_ALTA = 14;
    private static final double PRECIO_BASE_POR_PLAZA_Y_DIA = 20.0;

    /**
     * Constructor para la inyección de dependencias.
     * Spring Boot inyectará automáticamente las instancias de los repositorios
     * y servicios necesarios.
     * 
     * @param alquilerRepository Repositorio para operaciones de base de datos relacionadas con los alquileres.
     * @param reservaRepository Repositorio para el acceso a datos de Reservas.
     * @param socioRepository Repositorio para el acceso a datos de Socios.
     * @param embarcacionRepository Repositorio para el acceso a datos de Embarcaciones.
     */
    public AddAlquilerController(AlquilerRepository alquilerRepository, ReservaRepository reservaRepository, SocioRepository socioRepository, EmbarcacionRepository embarcacionRepository) {
        this.alquilerRepository = alquilerRepository;    
        this.reservaRepository = reservaRepository;
        this.socioRepository = socioRepository;
        this.embarcacionRepository = embarcacionRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.alquilerRepository.setSqlQueriesFileName(sqlQueriesFileName);  
        this.reservaRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }


    /**
     * Muestra el formulario para agregar un nuevo alquiler.
     * 
     * @return ModelAndView con el formulario de alquiler
     */
    @GetMapping("/addAlquiler")
    public ModelAndView mostrarFormularioAlquiler() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("alquiler/addAlquilerView");
        modelAndView.addObject("alquiler", new Alquiler());
        return modelAndView; 
    }

    /**
     * Procesa el formulario para agregar un nuevo alquiler.
     * 
     * @param alquiler Objeto Alquiler con los datos del formulario
     * @param status Controlador de estado de la sesión para limpiarla tras el envío.
     * @return ModelAndView con el mensaje de éxito o error
     */
    // Clean Code - Regla 10: Se han eliminado los comentarios explicativos línea a línea, confiando en la expresividad del código y los buenos nombres.
    @PostMapping("/addAlquiler")
    public ModelAndView procesarFormularioAlquiler(@ModelAttribute Alquiler alquiler, SessionStatus status) {
        ModelAndView modelAndView = new ModelAndView();

        String resultado;
        Socio socio = socioRepository.findSocioByDNI(alquiler.getUserNationalId());

        if (socio == null) {
            resultado = "El socio no existe.";
            modelAndView.setViewName("alquiler/addAlquilerView");
            modelAndView.addObject("mensajeError", resultado);
            status.setComplete();
            return modelAndView;
        }
        if (!socio.getHasSkipperLicense()) {
            resultado = "El socio no tiene título de patrón.";
            modelAndView.setViewName("alquiler/addAlquilerView");
            modelAndView.addObject("mensajeError", resultado);
            status.setComplete();
            return modelAndView;
        }

        LocalDate inicio = alquiler.getStartDate();
        LocalDate fin = alquiler.getEndDate();
        long totalDays = ChronoUnit.DAYS.between(inicio, fin) + 1;

        if (inicio.isAfter(fin)) {
            resultado = "La fecha de inicio no puede ser posterior a la de fin.";
            modelAndView.setViewName("alquiler/addAlquilerView");
            modelAndView.addObject("mensajeError", resultado);
            status.setComplete();
            return modelAndView;
        }

        int mesInicio = inicio.getMonthValue();
        if (mesInicio >= MES_INICIO_TEMPORADA_BAJA_OCT || mesInicio <= MES_FIN_TEMPORADA_BAJA_ABR) {
            if (totalDays > MAX_DIAS_ALQUILER_TEMPORADA_BAJA) {
                resultado = "Solo se permiten hasta 3 días entre octubre y abril.";
                modelAndView.setViewName("alquiler/addAlquilerView");
                modelAndView.addObject("mensajeError", resultado);
                status.setComplete();
                return modelAndView;
            }
        } else if (mesInicio >= MES_INICIO_TEMPORADA_ALTA_MAY && mesInicio <= MES_FIN_TEMPORADA_ALTA_SEP) {
            if (totalDays != SEMANA_TEMPORADA_ALTA && totalDays != QUINCENA_TEMPORADA_ALTA) {
                resultado = "Solo se permiten alquileres de 7 o 14 días entre mayo y septiembre.";
                modelAndView.setViewName("alquiler/addAlquilerView");
                modelAndView.addObject("mensajeError", resultado);
                status.setComplete();
                return modelAndView;
            }
        }

        Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(alquiler.getBoatRegistration());
        if (embarcacion == null) {
            resultado = "Embarcación no encontrada.";
            modelAndView.setViewName("alquiler/addAlquilerView");
            modelAndView.addObject("mensajeError", resultado);
            status.setComplete();
            return modelAndView;
        }
        if (alquiler.getSeats() > embarcacion.getSeats()) {
            resultado = "No hay suficientes plazas.";
            modelAndView.setViewName("alquiler/addAlquilerView");
            modelAndView.addObject("mensajeError", resultado);
            status.setComplete();
            return modelAndView;
        }

        List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcaciones();
        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        List<Embarcacion> availableBoats = new ArrayList<>();
        List<Reserva> reservas = reservaRepository.findAllReservas();

        if (embarcaciones == null) embarcaciones = new ArrayList<>();
        if (alquileres == null) alquileres = new ArrayList<>();
        if (reservas == null) reservas = new ArrayList<>();

        for (Embarcacion embarcacionEvaluada : embarcaciones) {
            boolean ocupada = false;

            for (Alquiler alquilerExistente : alquileres) {
                if (alquilerExistente.getBoatRegistration().equals(embarcacionEvaluada.getRegistration())) {
                    if (haySuperposicionConAlquiler(inicio, fin, alquilerExistente)) {
                        ocupada = true;
                        break;
                    }
                }
            }

            if (!ocupada) {
                for (Reserva reservaExistente : reservas) {
                    if (reservaExistente.getBoatRegistration().equals(embarcacionEvaluada.getRegistration())) {
                        if (fechaReservaEstaEnRangoAlquiler(reservaExistente.getDate(), inicio, fin)) {
                            ocupada = true;
                            break;
                        }
                    }
                }
            }

            if (!ocupada) {
                availableBoats.add(embarcacionEvaluada);
            }
        }

        boolean disponible = false;
        for (Embarcacion embarcacionDisponible : availableBoats) {
            if (embarcacionDisponible.getRegistration().equals(alquiler.getBoatRegistration())) {
                disponible = true;
                break;
            }
        }

        if (!disponible) {
            resultado = "La embarcación no está disponible en esas fechas.";
            modelAndView.setViewName("alquiler/addAlquilerView");
            modelAndView.addObject("mensajeError", resultado);
            status.setComplete();
            return modelAndView;
        }

        double priceInEuros = PRECIO_BASE_POR_PLAZA_Y_DIA * alquiler.getSeats() * totalDays;
        alquiler.setPrice(priceInEuros);

        boolean insertado = alquilerRepository.addAlquiler(alquiler);
        if (insertado) {
            resultado = "OK:" + alquiler.getId();
        } else {
            resultado = "Error al registrar el alquiler.";
        }

        if (resultado.startsWith("OK:")) {
            Integer alquilerId = Integer.parseInt(resultado.substring(3));
            int plazas = alquiler.getSeats();
            if (plazas > 1) {
                modelAndView.setViewName("redirect:/api/acompanantes/" + alquilerId + "/" + plazas);
            } else {
                modelAndView.setViewName("alquiler/addAlquilerView");
            }
        } else {
            modelAndView.setViewName("alquiler/addAlquilerView");
            modelAndView.addObject("mensajeError", resultado);
        }
        status.setComplete();

        return modelAndView;
    }


    // Clean Code - Regla 7: Se han eliminado los marcadores de separación visuales (ej. --- Métodos privados ---) para evitar ruido innecesario en el código.
    /**
     * Verifica si un rango de fechas [inicio, fin] se superpone con el período de un alquiler existente.
     * La superposición ocurre cuando el nuevo rango no termina antes de que empiece el alquiler
     * ni empieza después de que termine el alquiler.
     *
     * @param inicio            Fecha de inicio del nuevo período
     * @param fin               Fecha de fin del nuevo período
     * @param alquilerExistente Alquiler con el que se compara
     * @return true si existe superposición de fechas
     */
    private boolean haySuperposicionConAlquiler(LocalDate inicio, LocalDate fin, Alquiler alquilerExistente) {
        return !(inicio.isAfter(alquilerExistente.getEndDate()) || fin.isBefore(alquilerExistente.getStartDate()));
    }

    /**
     * Verifica si una fecha de reserva (que ocupa un único día) cae dentro del rango de fechas de un alquiler.
     *
     * @param fechaReserva Fecha concreta de la reserva
     * @param inicio       Fecha de inicio del rango de alquiler
     * @param fin          Fecha de fin del rango de alquiler
     * @return true si la fecha de reserva está dentro del rango [inicio, fin]
     */
    private boolean fechaReservaEstaEnRangoAlquiler(LocalDate fechaReserva, LocalDate inicio, LocalDate fin) {
        return !fechaReserva.isBefore(inicio) && !fechaReserva.isAfter(fin);
    }

}
