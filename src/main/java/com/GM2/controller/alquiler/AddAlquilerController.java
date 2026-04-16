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
    @PostMapping("/addAlquiler")
    public ModelAndView procesarFormularioAlquiler(@ModelAttribute Alquiler alquiler, SessionStatus status) {
    
        ModelAndView modelAndView = new ModelAndView();

        String resultado;
        Socio socio = socioRepository.findSocioByDNI(alquiler.getUserNationalId());

        if (socio == null){
            resultado = "El socio no existe.";
            modelAndView.setViewName("alquiler/addAlquilerView");
            modelAndView.addObject("mensajeError", resultado);
            status.setComplete();
            return modelAndView;
        }
        if (!socio.getHasSkipperLicense()){ 
            resultado = "El socio no tiene título de patrón.";
            modelAndView.setViewName("alquiler/addAlquilerView");
            modelAndView.addObject("mensajeError", resultado);
            status.setComplete();
            return modelAndView;
        }

        LocalDate inicio = alquiler.getStartDate();
        LocalDate fin = alquiler.getEndDate();
        // Clean Code - Reglas de nombrado: variable con unidad (dias -> totalDays)
        long totalDays = ChronoUnit.DAYS.between(inicio, fin) + 1;

        if (inicio.isAfter(fin)){ 
            resultado = "La fecha de inicio no puede ser posterior a la de fin.";
            modelAndView.setViewName("alquiler/addAlquilerView");
            modelAndView.addObject("mensajeError", resultado);
            status.setComplete();
            return modelAndView;
        }

        int mesInicio = inicio.getMonthValue();
        if (mesInicio >= 10 || mesInicio <= 4) {
            if (totalDays > 3){
                resultado = "Solo se permiten hasta 3 días entre octubre y abril.";
                modelAndView.setViewName("alquiler/addAlquilerView");
                modelAndView.addObject("mensajeError", resultado);
                status.setComplete();
                return modelAndView;
            }
        } else if (mesInicio >= 5 && mesInicio <= 9) {
            if (totalDays != 7 && totalDays != 14){ 
                resultado = "Solo se permiten alquileres de 7 o 14 días entre mayo y septiembre.";
                modelAndView.setViewName("alquiler/addAlquilerView");
                modelAndView.addObject("mensajeError", resultado);
                status.setComplete();
                return modelAndView;
            }
        }

        Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(alquiler.getBoatRegistration());
        if (embarcacion == null){
            resultado = "Embarcación no encontrada.";
            modelAndView.setViewName("alquiler/addAlquilerView");
            modelAndView.addObject("mensajeError", resultado);
            status.setComplete();
            return modelAndView;
        }
        if (alquiler.getSeats() > embarcacion.getSeats()){
            resultado = "No hay suficientes plazas.";
            modelAndView.setViewName("alquiler/addAlquilerView");
            modelAndView.addObject("mensajeError", resultado);
            status.setComplete();
            return modelAndView;
        }

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
                if (a.getBoatRegistration().equals(e.getRegistration())) {
                    if (!(inicio.isAfter(a.getEndDate()) || fin.isBefore(a.getStartDate()))) {
                        ocupada = true;
                        break;
                    }
                }
            }

            if (!ocupada) {
                for (Reserva r : reservas) {
                    if (r.getBoatRegistration().equals(e.getRegistration())) {
                        // Una reserva ocupa la embarcación por UN DÍA específico
                        // Verificar si alguna fecha del rango de alquiler coincide con la fecha de reserva
                        LocalDate fechaReserva = r.getDate();
                        if (!fechaReserva.isBefore(inicio) && !fechaReserva.isAfter(fin)) {
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
        
        boolean disponible = false;
        for (Embarcacion e : availableBoats) {
            if (e.getRegistration().equals(alquiler.getBoatRegistration())) {
                disponible = true;
                break;
            }
        }
        
        if (!disponible){ 
            resultado = "La embarcación no está disponible en esas fechas.";
            modelAndView.setViewName("alquiler/addAlquilerView");
            modelAndView.addObject("mensajeError", resultado);
            status.setComplete();
            return modelAndView;
        }


        // Clean Code - Reglas de nombrado: variable con unidad (precio -> priceInEuros )
        double priceInEuros = 20.0 * alquiler.getSeats() * totalDays;
        alquiler.setPrice(priceInEuros);

        boolean insertado = alquilerRepository.addAlquiler(alquiler);
        if (insertado) {
            resultado = "OK:" + alquiler.getId(); // devolvemos ID de alquiler para el siguiente paso
        } else {
            resultado = "Error al registrar el alquiler.";
        }

        if (resultado.startsWith("OK:") ){

        
            // Extraer el id del alquiler
            Integer alquilerId = Integer.parseInt(resultado.substring(3));
            int plazas = alquiler.getSeats();
            if( plazas > 1){
                modelAndView.setViewName("redirect:/api/acompanantes/" + alquilerId + "/" + plazas);
            }
            else{
                modelAndView.setViewName("alquiler/addAlquilerView");
            }
        } else {
            // Fallo: volvemos al formulario y mostramos mensaje de error
            modelAndView.setViewName("alquiler/addAlquilerView");
            modelAndView.addObject("mensajeError", resultado);
        }
        status.setComplete();

        return modelAndView;
    }


}
