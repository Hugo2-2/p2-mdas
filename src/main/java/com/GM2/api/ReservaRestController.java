package com.GM2.api;

import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Reserva;
import com.GM2.model.repository.AlquilerRepository;
import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.ReservaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador REST de la API de reservas.
 *
 * @author gm2equipo1
 * @version 1.0
 */

//Clean Code - Reglas de comentarios: Comentario redundate sobre @RestController
@RestController 
// Clean Code - Reglas de comentarios: Comentario redundate sobre @RequestMapping
@RequestMapping(path = "api/reservas", produces = "application/json")
public class ReservaRestController {

    // Repositorios necesarios para acceder a la BBDD.
    ReservaRepository reservaRepository;
    EmbarcacionRepository embarcacionRepository;
    AlquilerRepository alquilerRepository;

    // Constructor con inyección de dependencias.
    public ReservaRestController(ReservaRepository reservaRepository,
                                 EmbarcacionRepository embarcacionRepository,
                                 AlquilerRepository alquilerRepository) {
        this.reservaRepository = reservaRepository;
        this.embarcacionRepository = embarcacionRepository;
        this.alquilerRepository = alquilerRepository;

        // Configuración de la ruta del archivo SQL (necesario por la arquitectura de AbstractRepository).
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.reservaRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.alquilerRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    // Clean Code - Regla 7: Se han eliminado los marcadores de separación visuales (ej. === MÉTODOS GET ===) para evitar ruido innecesario en el código.
    /**
     * Obtener la lista completa de reservas (GET)
     *
     * @return ResponseEntity con la lista de reservas y estado 200 (OK) si se ha podido obtener correctamente,
     * 204 (No Content) si la lista está vacía, y en caso de error: 500 (Internal Server Error)
     */
    @GetMapping
    public ResponseEntity<List<Reserva>> getAllReservas() {
        try {
            List<Reserva> reservas = reservaRepository.findAllReservas();
            //Clean Code - Reglas de comentarios: Comentario redundate sobre código de estado
            if (reservas == null || reservas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            //Clean Code - Reglas de comentarios: Comentario redundate sobre código de estado
            return new ResponseEntity<>(reservas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtener la lista de reservas futuras dada una fecha (GET)
     *
     * @param fecha Fecha de referencia para filtrar las reservas futuras
     * @return ResponseEntity con la lista de reservas futuras y estado 200 (OK) si se ha podido obtener correctamente,
     * 204 (No Content) si no hay resultados, y en caso de error: 500 (Internal Server Error)
     */
    @GetMapping(params = "fecha")
    public ResponseEntity<List<Reserva>> getReservasFuturas(@RequestParam LocalDate fecha) {
        try {
            // Traemos todas las reservas porque el filtrado se hace en memoria (Java).
            List<Reserva> todasReservas = reservaRepository.findAllReservas();
            List<Reserva> reservasFuturas = new ArrayList<>();

            if (todasReservas != null) {
                for (Reserva reserva : todasReservas) {
                    // Filtramos: Solo añadimos si la fecha de la reserva es POSTERIOR a la solicitada.
                    if (reserva.getDate().isAfter(fecha)) {
                        reservasFuturas.add(reserva);
                    }
                }
            }

            if (reservasFuturas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(reservasFuturas, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtener la información concreta de una reserva dado su identificador (GET)
     *
     * @param id Identificador de la reserva
     * @return ResponseEntity con la información de la reserva y estado 200 (OK) si se ha podido obtener correctamente
     * y en caso de error: 404 (Not Found) o 500 (Internal Server Error)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable Integer id) {
        try {
            Reserva reserva = reservaRepository.findReservaById(id);
            //Clean Code - Reglas de comentarios: Comentario redundate sobre código de estado
            if (reserva == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(reserva, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Crea una nueva reserva (POST)
     *
     * @param nuevaReserva Reserva a crear
     * @return ResponseEntity con la reserva creada y estado 201 (Created) si se ha podido crear correctamente
     * y en caso de error: 409 (Conflict), 422 (Unprocessable Entity) o 500 (Internal Server Error)
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Reserva> createReserva(@RequestBody Reserva nuevaReserva) {
        try {
            // 1. VALIDACIÓN: ¿Existe la embarcación?
            Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(nuevaReserva.getBoatRegistration());
            if (embarcacion == null) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            // 2. VALIDACIÓN: Capacidad.
            // Plazas solicitadas + 1 (Patrón) no puede superar las plazas del barco.
            if ((nuevaReserva.getSeats() + 1) > embarcacion.getSeats()) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            // 3. VALIDACIÓN: Disponibilidad.
            // Usamos el método auxiliar para ver si choca con alquileres o reservas existentes.
            if (isEmbarcacionOcupada(nuevaReserva.getBoatRegistration(), nuevaReserva.getDate())) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT); // 409 Conflict
            }

            // 4. LOGICA DE NEGOCIO: Calcular precio.
            // Clean Code - Reglas de comentarios: Comentario que solo tiene sentido para el programador que lo escribió (Comentario reemplazado) 
            // Calculamos el precio en el servidor para preveer manipulación desde el cliente
            // Ignoramos cualquier precio que venga en el JSON para evitar fraudes.

            // Clean Code - Reglas de nombrado: variable con unidad (precioCalculado -> calculatedPriceInEuros )
            double calculatedPriceInEuros = 40.0 * nuevaReserva.getSeats();
            nuevaReserva.setPrice(calculatedPriceInEuros);

            // 5. PERSISTENCIA: Guardar en BBDD.
            boolean exito = reservaRepository.addReserva(nuevaReserva);

            //Clean Code - Reglas de comentarios: Comentario redundate sobre código de estado
            if (exito) return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
            else return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Modificar la fecha de una reserva futura (PATCH)
     *
     * @param id Identificador de la reserva
     * @param datosNuevos Objeto Reserva con la nueva fecha
     * @return ResponseEntity con la reserva actualizada y estado 200 (OK) si se ha podido actualizar correctamente
     * y en caso de error: 400 (Bad Request), 404 (Not Found), 409 (Conflict) o 500 (Internal Server Error)
     */
    @PatchMapping("/{id}/fecha")
    public ResponseEntity<Reserva> updateReservaFecha(@PathVariable Integer id, @RequestBody Reserva datosNuevos) {
        try {
            // Buscamos la reserva original.
            Reserva reservaExistente = reservaRepository.findReservaById(id);
            if (reservaExistente == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            LocalDate nuevaFecha = datosNuevos.getDate();
            if (nuevaFecha == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            // VALIDACIÓN: No se puede mover una reserva al pasado.
            if (nuevaFecha.isBefore(LocalDate.now())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // VALIDACIÓN: Disponibilidad en la nueva fecha.
            // Reutilizamos el método auxiliar. Si devuelve true, es que está ocupada.
            if (isEmbarcacionOcupada(reservaExistente.getBoatRegistration(), nuevaFecha)) {
                return new ResponseEntity<>(HttpStatus.CONFLICT); // El barco ya está cogido ese día.
            }

            // Aplicamos el cambio y guardamos.
            reservaExistente.setDate(nuevaFecha);
            boolean exito = reservaRepository.updateReserva(reservaExistente);

            if (exito) return new ResponseEntity<>(reservaExistente, HttpStatus.OK);
            else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Modificar descripción y plazas de una reserva (PATCH)
     *
     * @param id Identificador de la reserva
     * @param datosNuevos Objeto Reserva con los nuevos detalles
     * @return ResponseEntity con la reserva actualizada y estado 200 (OK) si se ha podido actualizar correctamente
     * y en caso de error: 404 (Not Found), 422 (Unprocessable Entity) o 500 (Internal Server Error)
     */
    @PatchMapping("/{id}/detalles")
    public ResponseEntity<Reserva> updateReservaDetalles(@PathVariable Integer id, @RequestBody Reserva datosNuevos) {
        try {
            Reserva reservaExistente = reservaRepository.findReservaById(id);
            if (reservaExistente == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            // Si viene descripción, la actualizamos.
            if (datosNuevos.getDescription() != null) {
                reservaExistente.setDescription(datosNuevos.getDescription());
            }

            // Si vienen plazas, validamos capacidad antes de actualizar.
            if (datosNuevos.getSeats() > 0) {
                Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(reservaExistente.getBoatRegistration());

                // Clean Code - Reglas de comentarios: Comentario que explica intención del código
                if (embarcacion != null && (datosNuevos.getSeats() + 1) > embarcacion.getSeats()) {
                    return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
                }

                // 1. Actualizamos las plazas
                reservaExistente.setSeats(datosNuevos.getSeats());

                // Clean Code - Reglas de comentarios: Comentario que solo tiene sentido para el programador que lo escribió
                // Clean Code - Reglas de nombrado: variable con unidad (precioCalculado -> calculatedPriceInEuros )
                double calculatedPriceInEuros = 40.0 * datosNuevos.getSeats();
                reservaExistente.setPrice(calculatedPriceInEuros);
            }

            boolean exito = reservaRepository.updateReserva(reservaExistente);

            if (exito) return new ResponseEntity<>(reservaExistente, HttpStatus.OK);
            else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Cancelar una reserva existente (DELETE)
     *
     * @param id Identificador de la reserva
     * @return ResponseEntity con estado 204 (No Content) si se ha podido cancelar correctamente
     * y en caso de error: 400 (Bad Request), 404 (Not Found) o 500 (Internal Server Error)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Integer id) {
        try {
            Reserva reserva = reservaRepository.findReservaById(id);
            if (reserva == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            // VALIDACIÓN: No permitir cancelar reservas que ya han pasado.
            if (reserva.getDate().isBefore(LocalDate.now())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            boolean exito = reservaRepository.deleteReserva(id);

            //Clean Code - Reglas de comentarios: Comentario redundate sobre código de estado
            if (exito) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Método auxiliar para comprobar si una embarcación está ocupada en una fecha específica.
     * Revisa conflictos tanto en la tabla de ALQUILERES como en la de RESERVAS.
     *
     * @param matricula Matrícula de la embarcación
     * @param fecha Fecha a comprobar
     * @return true si está ocupada, false si está libre
     */
    private boolean isEmbarcacionOcupada(String matricula, LocalDate fecha) {
        // 1. Comprobar conflictos con ALQUILERES (Rango de fechas).
        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        if (alquileres != null) {
            for (Alquiler alquiler : alquileres) {
                if (alquiler.getBoatRegistration().equals(matricula)) {
                    // Lógica: Si la fecha solicitada NO es antes del inicio Y NO es después del fin,
                    // significa que está DENTRO del periodo de alquiler.
                    if (!fecha.isBefore(alquiler.getStartDate()) && !fecha.isAfter(alquiler.getEndDate())) {
                        return true; // Conflicto encontrado
                    }
                }
            }
        }

        // 2. Comprobar conflictos con OTRAS RESERVAS (Fecha única).
        List<Reserva> reservas = reservaRepository.findAllReservas();
        if (reservas != null) {
            for (Reserva r : reservas) {
                // Si es el mismo barco y el mismo día -> Conflicto.
                if (r.getBoatRegistration().equals(matricula) && r.getDate().equals(fecha)) {
                    return true;
                }
            }
        }
        return false; // Está libre
    }
}