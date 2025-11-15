package com.GM2.controller.reserva;

import com.GM2.model.domain.Alquiler; // Clase de dominio para Alquiler
import com.GM2.model.domain.Embarcacion; // Clase de dominio para Embarcacion
import com.GM2.model.domain.Reserva; // Clase de dominio para Reserva
import com.GM2.model.domain.Socio; // Clase de dominio para Socio
import com.GM2.model.repository.AlquilerRepository; // Repositorio para acceder a datos de Alquiler
import com.GM2.model.repository.EmbarcacionRepository; // Repositorio para acceder a datos de Embarcacion
import com.GM2.model.repository.ReservaRepository; // Repositorio para acceder a datos de Reserva
import com.GM2.model.repository.SocioRepository; // Repositorio para acceder a datos de Socio

import org.springframework.stereotype.Controller; // Marca la clase como un controlador de Spring MVC
import org.springframework.web.bind.annotation.*; // Importa anotaciones web (GetMapping, PostMapping, RequestMapping, etc.)
import org.springframework.web.bind.support.SessionStatus; // Para manejar el estado de la sesión
import org.springframework.web.servlet.ModelAndView; // Para manejar la vista y el modelo

import java.time.LocalDate; // Para trabajar con fechas
import java.util.List; // Para manejar colecciones

/**
 * Controlador web (MVC) para la gestión de Reservas.
 * Sigue la misma estructura que AddAlquilerController.
 */
@Controller
// Define la ruta base para todos los métodos de este controlador
@RequestMapping("/api/reserva")
public class AddReservaController {

    // Inyección de dependencias de los repositorios
    ReservaRepository reservaRepository;
    EmbarcacionRepository embarcacionRepository;
    SocioRepository socioRepository;
    AlquilerRepository alquilerRepository;

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
        modelAndView.setViewName("addReservaView");
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
        modelAndView.setViewName("addReservaView");
        String resultado; // Variable para almacenar el mensaje de éxito o error

        // 1. Validar el Socio
        // Busca al socio en la base de datos usando el DNI (usuario_id) de la reserva
        Socio socio = socioRepository.findSocioByDNI(reserva.getUsuario_id());

        // --- VALIDACIONES ---
        if (socio == null) {
            resultado = "El DNI del socio no está registrado.";
            modelAndView.addObject("mensajeError", resultado);
            status.setComplete();
            return modelAndView;

        } else if (!socio.esMayorEdad()) {
            resultado = "El socio debe ser mayor de edad para realizar una reserva.";
            modelAndView.addObject("mensajeError", resultado);
            status.setComplete();
            return modelAndView;

        } else {
            // 2. Validar la Embarcación
            Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(reserva.getMatricula_embarcacion());
            if (embarcacion == null) {
                resultado = "La embarcación no existe.";
                modelAndView.addObject("mensajeError", resultado);
                status.setComplete();
                return modelAndView;

            } else if (embarcacion.getIdPatron() == null || embarcacion.getIdPatron().isEmpty()) {
                resultado = "La embarcación no tiene patrón asignado.";
                modelAndView.addObject("mensajeError", resultado);
                status.setComplete();
                return modelAndView;

            } else if (embarcacion.getPlazas() < reserva.getPlazas() + 1) {
                // Se suma 1 a las plazas reservadas para contar al patrón
                resultado = "Capacidad insuficiente (recuerda sumar 1 para el patrón).";
                modelAndView.addObject("mensajeError", resultado);
                status.setComplete();
                return modelAndView;

            } else {
                // --- DISPONIBILIDAD ---
                // 3. Validar Disponibilidad de la Embarcación
                LocalDate fecha = reserva.getFecha();
                boolean disponible = true;

                // Obtiene todos los alquileres y reservas existentes para verificar conflictos
                List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
                List<Reserva> reservas = reservaRepository.findAllReservas();

                // 3.1. Verificar conflictos con Alquileres
                for (Alquiler a : alquileres) {
                    // Conflicto si la matrícula es la misma Y la fecha de reserva cae dentro del rango de alquiler
                    if (a.getMatricula_embarcacion().equals(embarcacion.getMatricula())
                            // `!fecha.isBefore(a.getFechainicio)`: la fecha es igual o posterior a la fecha de inicio
                            && !fecha.isBefore(a.getFechainicio())
                            // `!fecha.isAfter(a.getFechafin)`: la fecha es igual o anterior a la fecha de fin
                            && !fecha.isAfter(a.getFechafin())) {
                        disponible = false;
                        break;
                    }
                }

                // 3.2. Verificar conflictos con otras Reservas (si ya se encontró un conflicto con Alquileres, se salta)
                if (disponible) {
                    for (Reserva r : reservas) {
                        // Conflicto si la matrícula es la misma Y la fecha de reserva es la misma
                        if (r.getMatricula_embarcacion().equals(embarcacion.getMatricula())
                                && r.getFecha().equals(fecha)) {
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
                    // Calcula y establece el precio de la reserva (Plazas * 40.0)
                    reserva.setPrecio(reserva.getPlazas() * 40.0);
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
            modelAndView.setViewName("addReservaView");
            // Añade el mensaje de error al modelo para que se muestre en la vista
            modelAndView.addObject("mensajeError", resultado);
        }

        // Completa el estado de la sesión si fuera necesario (buena práctica)
        status.setComplete();
        return modelAndView;
    }
}