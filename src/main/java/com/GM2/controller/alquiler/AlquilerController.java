package com.GM2.controller.alquiler;

import com.GM2.model.domain.Acompanante;
import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Reserva;
import com.GM2.model.domain.Socio;
import com.GM2.model.repository.AcompananteRepository;
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
 * Maneja las peticiones web para mostrar formularios y procesar la
 * creación de nuevos alquileres.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
@Controller
@RequestMapping("/api/alquiler")
public class AlquilerController {

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
    public AlquilerController(AlquilerRepository alquilerRepository, AcompananteRepository acompanantesRepository, ReservaRepository reservaRepository, SocioRepository socioRepository, EmbarcacionRepository embarcacionRepository) {
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
     * Muestra el formulario para agregar un nuevo alquiler.
     * 
     * @return ModelAndView con el formulario de alquiler
     */
    @GetMapping("/addAlquiler")
    public ModelAndView mostrarFormularioAlquiler() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addAlquilerView");
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
        Socio socio = socioRepository.findSocioByDNI(alquiler.getUsuario_dni());

        if (socio == null) resultado = "El socio no existe.";
        if (!socio.getTieneLicenciaPatron()) resultado = "El socio no tiene título de patrón.";

        LocalDate inicio = alquiler.getFechainicio();
        LocalDate fin = alquiler.getFechafin();
        long dias = ChronoUnit.DAYS.between(inicio, fin) + 1;

        if (inicio.isAfter(fin)) resultado = "La fecha de inicio no puede ser posterior a la de fin.";

        int mesInicio = inicio.getMonthValue();
        if (mesInicio >= 10 || mesInicio <= 4) {
            if (dias > 3) resultado = "Solo se permiten hasta 3 días entre octubre y abril.";
        } else if (mesInicio >= 5 && mesInicio <= 9) {
            if (dias != 7 && dias != 14) resultado = "Solo se permiten alquileres de 7 o 14 días entre mayo y septiembre.";
        }

        Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(alquiler.getMatricula_embarcacion());
        if (embarcacion == null) resultado = "Embarcación no encontrada.";
        if (alquiler.getPlazas() > embarcacion.getPlazas()) resultado = "No hay suficientes plazas.";

        List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcaciones();
        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        List<Embarcacion> disponibles = new ArrayList<>();
        List<Reserva> reservas = reservaRepository.findAllReservas();

        // Comprobar null
        if (embarcaciones == null) embarcaciones = new ArrayList<>();
        if (alquileres == null) alquileres = new ArrayList<>();
        if (reservas == null) reservas = new ArrayList<>();


        for (Embarcacion e : embarcaciones) {
            boolean ocupada = false;

            for (Alquiler a : alquileres) {
                if (a.getMatricula_embarcacion().equals(e.getMatricula())) {
                    if (!(fin.isBefore(a.getFechainicio()) || inicio.isAfter(a.getFechafin()))) {
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
                        if (!fechaReserva.isBefore(inicio) && !fechaReserva.isAfter(fin)) {
                            ocupada = true;
                            break;
                        }
                    }
                }
            }
            
            if (!ocupada) {
                disponibles.add(e);
            }
        }
        
        boolean disponible = false;
        for (Embarcacion e : disponibles) {
            if (e.getMatricula().equals(alquiler.getMatricula_embarcacion())) {
                disponible = true;
                break;
            }
        }
        if (!disponible) resultado = "La embarcación no está disponible en esas fechas.";



        double precio = 20.0 * alquiler.getPlazas() * dias;
        alquiler.setPrecio(precio);

        boolean insertado = alquilerRepository.addAlquiler(alquiler);
        if (insertado) {
            resultado = "OK:" + alquiler.getId(); // devolvemos ID de alquiler para el siguiente paso
        } else {
            resultado = "Error al registrar el alquiler.";
        }

        if (resultado.startsWith("OK:") ){

        
            // Extraer el id del alquiler
            Integer alquilerId = Integer.parseInt(resultado.substring(3));
            int plazas = alquiler.getPlazas();

        modelAndView.setViewName("redirect:/api/alquiler/acompanantes/" + alquilerId + "/" + plazas);
    
        } else {
            // Fallo: volvemos al formulario y mostramos mensaje de error
            modelAndView.setViewName("addAlquilerView");
            modelAndView.addObject("mensajeError", resultado);
        }
        status.setComplete();

        return modelAndView;
    }

    /**
     * Muestra el formulario para gestionar acompañantes de un alquiler.
     * 
     * @param alquilerId ID del alquiler
     * @param plazas Número de plazas disponibles
     * @return ModelAndView con el formulario de acompañantes
     */
    @GetMapping("/acompanantes/{alquilerId}/{plazas}")
    public ModelAndView mostrarFormularioAcompanantes(@PathVariable Integer alquilerId, @PathVariable int plazas) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addAcompanante");

        // 1. Cargar acompañantes existentes de la base de datos
        List<Acompanante> acompanantesExistentes = acompanantesRepository.findAcompananteByAlquiler(alquilerId);
        
        // Si es null, crear lista vacía
        if (acompanantesExistentes == null) {
            acompanantesExistentes = new ArrayList<>();
        }

        // 2. Calcular cuántos acompañantes nuevos se pueden añadir
        int plazasDisponibles = plazas - 1 - acompanantesExistentes.size();
        
        // 3. Crear lista para nuevos acompañantes (solo los que caben)
        List<Acompanante> nuevosAcompanantes = new ArrayList<>();
        for (int i = 0; i < plazasDisponibles; i++) {
            nuevosAcompanantes.add(new Acompanante());
        }

        // 4. Pasar todos los datos al modelo
        modelAndView.addObject("alquilerId", alquilerId);
        modelAndView.addObject("plazas", plazas);
        modelAndView.addObject("acompanantesExistentes", acompanantesExistentes);
        modelAndView.addObject("acompanantes", nuevosAcompanantes); // Los nuevos que se pueden añadir
        modelAndView.addObject("plazasDisponibles", plazasDisponibles);

        return modelAndView;
    }


    /**
     * Obtiene todos los alquileres del sistema.
     * 
     * @return ModelAndView con la lista de alquileres
     */
    @GetMapping
    public ModelAndView getAlquileres() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("listAlquiler");
        
        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        modelAndView.addObject("alquileres", alquileres);
        
        return modelAndView;
    }

    /**
     * Obtiene los detalles de un alquiler específico.
     * 
     * @param id ID del alquiler
     * @return ModelAndView con los detalles del alquiler
     */
    @GetMapping("/{id}")
    public ModelAndView getAlquilerDetalles(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("detallesAlquiler");
        
        Alquiler alquiler = alquilerRepository.findAlquilerById(id);
        modelAndView.addObject("alquiler", alquiler);
        
        return modelAndView;
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
        modelAndView.setViewName("alquilerDisponible");
        
        // Si no hay parámetros, mostrar solo el formulario
        if (inicio == null || fin == null) {
            return modelAndView;
        }
        
        // Si hay parámetros, procesar y mostrar resultados
        try {
            LocalDate fechaInicio = LocalDate.parse(inicio);
            LocalDate fechaFin = LocalDate.parse(fin);
        
        // Buscar embarcaciones disponibles entre dos fechas
        List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcaciones();
        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        List<Embarcacion> disponibles = new ArrayList<>();
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
                disponibles.add(e);
            }
        }            
            modelAndView.addObject("disponibles", disponibles);
            modelAndView.addObject("fechaInicio", fechaInicio);
            modelAndView.addObject("fechaFin", fechaFin);
            modelAndView.addObject("mostrarResultados", true);
            
        } catch (Exception e) {
            modelAndView.addObject("error", "Formato de fecha incorrecto");
        }
        
        return modelAndView;
    }
    

    /**
     * Obtiene todos los alquileres futuros.
     * 
     * @return ModelAndView con la lista de alquileres futuros
     */
    @GetMapping("/futuros")
    public ModelAndView getAlquileresFuturos() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("alquileresFuturos");
        
        List<Alquiler> alquileresFuturos = alquilerRepository.listarAlquileresFuturos();
        modelAndView.addObject("alquileresFuturos", alquileresFuturos);
        
        return modelAndView;
    }

}
