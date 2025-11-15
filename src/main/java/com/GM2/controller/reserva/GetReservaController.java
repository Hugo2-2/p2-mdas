package com.GM2.controller.reserva;

import com.GM2.model.domain.Reserva; // Clase de dominio para Reserva
import com.GM2.model.repository.*; // Importa todos los repositorios (ReservaRepository, EmbarcacionRepository, etc.)
import org.springframework.stereotype.Controller; // Marca la clase como un controlador de Spring MVC
import org.springframework.web.bind.annotation.GetMapping; // Mapea peticiones HTTP GET
import org.springframework.web.bind.annotation.PathVariable; // Para extraer variables de la URL
import org.springframework.web.bind.annotation.RequestMapping; // Define la ruta base del controlador
import org.springframework.web.servlet.ModelAndView; // Para manejar la vista y el modelo

import java.util.List; // Para manejar colecciones de objetos

/**
 * Controlador encargado de mostrar las reservas y sus detalles.
 * Estructura paralela a GetAlquilerController.
 */
@Controller
// Define la ruta base para todas las solicitudes manejadas por este controlador
@RequestMapping("/api/reserva")
public class GetReservaController {

    // Inyección de dependencias de los repositorios
    ReservaRepository reservaRepository;
    EmbarcacionRepository embarcacionRepository;
    SocioRepository socioRepository;
    AlquilerRepository alquilerRepository;

    // Constructor que recibe todos los repositorios (Inyección por Constructor)
    public GetReservaController(
            ReservaRepository reservaRepository,
            EmbarcacionRepository embarcacionRepository,
            SocioRepository socioRepository,
            AlquilerRepository alquilerRepository
    ) {
        // Asigna los repositorios inyectados a las variables de instancia
        this.reservaRepository = reservaRepository;
        this.embarcacionRepository = embarcacionRepository;
        this.socioRepository = socioRepository;
        this.alquilerRepository = alquilerRepository;

        // Configura la ruta del archivo de propiedades SQL para todos los repositorios
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.reservaRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.alquilerRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Muestra todas las reservas registradas.
     * Mapeado a GET /api/reserva
     *
     * @return ModelAndView que contiene la lista de reservas y el nombre de la vista.
     */
    @GetMapping
    public ModelAndView getReservas() {
        ModelAndView modelAndView = new ModelAndView();
        // Establece la vista a la que se debe enviar el modelo
        modelAndView.setViewName("listReservaView");

        // Llama al repositorio para obtener todas las reservas
        List<Reserva> reservas = reservaRepository.findAllReservas();
        // Agrega la lista de reservas al modelo bajo el nombre "reservas"
        modelAndView.addObject("reservas", reservas);

        return modelAndView;
    }

    /**
     * Muestra los detalles de una reserva específica.
     * Mapeado a GET /api/reserva/{id}, donde {id} es una variable de la ruta.
     *
     * @param id El ID de la reserva a buscar, extraído de la URL.
     * @return ModelAndView que contiene el objeto Reserva y el nombre de la vista de detalles.
     */
    @GetMapping("/{id}")
    public ModelAndView getReservaDetalles(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        // Establece la vista para mostrar los detalles
        modelAndView.setViewName("detallesReservaView");

        // Llama al repositorio para buscar la reserva por su ID
        Reserva reserva = reservaRepository.findReservaById(id);
        // Agrega el objeto Reserva individual al modelo
        modelAndView.addObject("reserva", reserva);

        return modelAndView;
    }
}