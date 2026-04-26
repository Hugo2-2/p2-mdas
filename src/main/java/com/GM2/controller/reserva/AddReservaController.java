//Clean Code - Reglas de comentarios: Comentario redundate sobre importaciones
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
// Define la ruta base para todos los métodos de este controlador
@RequestMapping("/api/reserva")
public class AddReservaController {

    // Inyección de dependencias de los repositorios
    // Clean Code - Regla 8: Nombres largos y descriptivos ('reservaRepository', etc.) para variables de clase de ámbito global.
    ReservaRepository reservaRepository;
    EmbarcacionRepository embarcacionRepository;
    SocioRepository socioRepository;
    AlquilerRepository alquilerRepository;

    // Clean Code - Regla 7: Extracción de valores numéricos constantes a constantes descriptivas para evitar números mágicos y mejorar la legibilidad.
    private static final int PLAZAS_OCUPADAS_POR_PATRON = 1;
    private static final double PRECIO_POR_PLAZA_RESERVA = 40.0;

    // Constructor que recibe todos los repositorios (Inyección por constructor)
    public AddReservaController(
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
        // Nota: Esta configuración podría ser mejor manejada en AbstractRepository o a través de la configuración de Spring.
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.reservaRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.alquilerRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Muestra el formulario para agregar una nueva reserva.
     * Mapeado a GET /api/reserva/addReserva
     */
    @GetMapping("/addReserva")
    public ModelAndView mostrarFormularioReserva() {
        ModelAndView modelAndView = new ModelAndView();
        // Establece el nombre de la vista a mostrar (ej. addReservaView.jsp)
        modelAndView.setViewName("reserva/addReservaView");
        // Añade un objeto Reserva vacío al modelo para ser rellenado por el formulario
        modelAndView.addObject("reserva", new Reserva());
        return modelAndView;
    }

    /**
     * Procesa el formulario para agregar una nueva reserva.
     * Mapeado a POST /api/reserva/addReserva
     *
     * @param reserva Objeto Reserva rellenado con los datos del formulario (@ModelAttribute)
     * @param status Objeto para completar la sesión si se usa @SessionAttributes (aunque no se usa aquí)
     * @return ModelAndView para redirigir o mostrar un error
     */
    @PostMapping("/addReserva")
    public ModelAndView procesarFormularioReserva(@ModelAttribute Reserva reserva, SessionStatus status) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("reserva/addReservaView");
        String resultado; // Variable para almacenar el mensaje de éxito o error

        // 1. Validar el Socio
        // Busca al socio en la base de datos usando el DNI (usuario_id) de la reserva
        Socio socio = socioRepository.findSocioByDNI(reserva.getUserNationalId());

        // --- VALIDACIONES ---
        if (socio == null) {
            resultado = "El DNI del socio no está registrado.";
            modelAndView.addObject("mensajeError", resultado);
            status.setComplete();
            return modelAndView;
        // Clean Code - Regla de función: Función más pura posible
        } else if (!socio.isOfLegalAge(LocalDate.now())) {
            resultado = "El socio debe ser mayor de edad para realizar una reserva.";
            modelAndView.addObject("mensajeError", resultado);
            status.setComplete();
            return modelAndView;

        } else {
            // 2. Validar la Embarcación
            Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(reserva.getBoatRegistration());
            if (embarcacion == null) {
                resultado = "La embarcación no existe.";
                modelAndView.addObject("mensajeError", resultado);
                status.setComplete();
                return modelAndView;

            } else if (embarcacion.getSkipperId() == null || embarcacion.getSkipperId().isEmpty()) {
                resultado = "La embarcación no tiene patrón asignado.";
                modelAndView.addObject("mensajeError", resultado);
                status.setComplete();
                return modelAndView;

            // Clean Code - Regla 7: Extracción del valor numérico '1' a la constante 'PLAZAS_OCUPADAS_POR_PATRON' para evitar números mágicos y mejorar la legibilidad.
            } else if (embarcacion.getSeats() < reserva.getSeats() + PLAZAS_OCUPADAS_POR_PATRON) {
                // Se suman las plazas del patrón a las plazas reservadas
                resultado = "Capacidad insuficiente (recuerda sumar 1 para el patrón).";
                modelAndView.addObject("mensajeError", resultado);
                status.setComplete();
                return modelAndView;

            } else {
                // --- DISPONIBILIDAD ---
                // 3. Validar Disponibilidad de la Embarcación
                LocalDate fecha = reserva.getDate();
                boolean disponible = true;

                // Obtiene todos los alquileres y reservas existentes para verificar conflictos
                List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
                List<Reserva> reservas = reservaRepository.findAllReservas();

                // 3.1. Verificar conflictos con Alquileres
                // Clean Code - Regla 8: Uso de variable de un carácter ('a') apropiado por ser el índice de un bucle de muy corto alcance.
                for (Alquiler a : alquileres) {
                    // Clean Code - Regla 8: Se ha eliminado código obsoleto que estaba comentado, delegando el historial al control de versiones.
                    if (a.getBoatRegistration().equals(embarcacion.getRegistration())
                            && !fecha.isBefore(a.getStartDate())
                            && !fecha.isAfter(a.getEndDate())) {
                        disponible = false;
                        break;
                    }
                }

                // 3.2. Verificar conflictos con otras Reservas (si ya se encontró un conflicto con Alquileres, se salta)
                if (disponible) {
                    for (Reserva r : reservas) {
                        // Conflicto si la matrícula es la misma Y la fecha de reserva es la misma
                        if (r.getBoatRegistration().equals(embarcacion.getRegistration())
                                && r.getDate().equals(fecha)) {
                            disponible = false;
                            break;
                        }
                    }
                }


                if (!disponible) {
                    resultado = "La embarcación no está disponible en la fecha seleccionada.";
                } else {
                    // --- GUARDAR RESERVA ---
                    // 4. Procesar y Guardar
                    // Calcula y establece el precio de la reserva
                    // Clean Code - Regla 7: Extracción del valor numérico '40.0' a la constante 'PRECIO_POR_PLAZA_RESERVA' para evitar números mágicos y mejorar la legibilidad.
                    reserva.setPrice(reserva.getSeats() * PRECIO_POR_PLAZA_RESERVA);
                    // Llama al repositorio para insertar la reserva en la base de datos
                    boolean insertado = reservaRepository.addReserva(reserva);
                    resultado = insertado ? "EXITO" : "Error al guardar la reserva.";

                }
            }
        }

        // 5. Manejo de la Vista Final
        if ("EXITO".equals(resultado)) {
            // Si la inserción fue exitosa, redirige al listado de reservas (ej. /api/reserva)
            modelAndView.setViewName("redirect:/api/reserva");
        } else {
            // Si hay un error, vuelve a mostrar el formulario
            modelAndView.setViewName("reserva/addReservaView");
            // Añade el mensaje de error al modelo para que se muestre en la vista
            modelAndView.addObject("mensajeError", resultado);
        }

        // Completa el estado de la sesión si fuera necesario (buena práctica)
        status.setComplete();
        return modelAndView;
    }
}