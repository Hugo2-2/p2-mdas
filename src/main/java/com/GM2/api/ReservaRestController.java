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
 * Controlador REST para la gestión de Reservas (Actividades).
 * <p>
 * Este controlador maneja las peticiones HTTP definidas en la Práctica 2:
 * - Semana 1: GET (Listados) y POST (Creación).
 * - Semana 2 (Apartado D): PATCH (Modificación) y DELETE (Cancelación)[cite: 101].
 * </p>
 */
@RestController // Indica que es un componente de controlador REST (devuelve JSON, no HTML).
@RequestMapping(path = "api/reservas", produces = "application/json") // Define la URL base común para todos los métodos.
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

    // ========================================================================================
    // MÉTODOS GET (LECTURA) - SEMANA 1
    // ========================================================================================

    /**
     * Obtiene la lista completa de reservas.
     * Requisito: D.1 de Semana 1[cite: 69].
     */
    @GetMapping
    public ResponseEntity<List<Reserva>> getAllReservas() {
        try {
            List<Reserva> reservas = reservaRepository.findAllReservas();
            // Si la lista está vacía, devolvemos 204 (No Content) según buenas prácticas REST.
            if (reservas == null || reservas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            // Si hay datos, devolvemos la lista y 200 (OK).
            return new ResponseEntity<>(reservas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene reservas futuras filtradas por fecha.
     * Requisito: D.2 de Semana 1[cite: 70].
     * Uso: GET /api/reservas?fecha=2023-12-01
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
                    if (reserva.getFecha().isAfter(fecha)) {
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
     * Obtiene una reserva específica por su ID.
     * Requisito: D.3 de Semana 1[cite: 71].
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable Integer id) {
        try {
            Reserva reserva = reservaRepository.findReservaById(id);
            // Si no existe, devolvemos 404 Not Found.
            if (reserva == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(reserva, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ========================================================================================
    // MÉTODO POST (CREACIÓN) - SEMANA 1
    // ========================================================================================

    /**
     * Crea una nueva reserva.
     * Requisito: D.4 de Semana 1[cite: 72].
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Reserva> createReserva(@RequestBody Reserva nuevaReserva) {
        try {
            // 1. VALIDACIÓN: ¿Existe la embarcación?
            Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(nuevaReserva.getMatricula_embarcacion());
            if (embarcacion == null) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            // 2. VALIDACIÓN: Capacidad.
            // Plazas solicitadas + 1 (Patrón) no puede superar las plazas del barco.
            if ((nuevaReserva.getPlazas() + 1) > embarcacion.getPlazas()) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            // 3. VALIDACIÓN: Disponibilidad.
            // Usamos el método auxiliar para ver si choca con alquileres o reservas existentes.
            if (isEmbarcacionOcupada(nuevaReserva.getMatricula_embarcacion(), nuevaReserva.getFecha())) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT); // 409 Conflict
            }

            // 4. LOGICA DE NEGOCIO: Calcular precio.
            // CAMBIO: Al igual que en AddReservaController, FORZAMOS el cálculo en el servidor.
            // Ignoramos cualquier precio que venga en el JSON para evitar fraudes.
            double precioCalculado = 40.0 * nuevaReserva.getPlazas();
            nuevaReserva.setPrecio(precioCalculado);

            // 5. PERSISTENCIA: Guardar en BBDD.
            boolean exito = reservaRepository.addReserva(nuevaReserva);

            if (exito) return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED); // 201 Created
            else return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ========================================================================================
    // MÉTODOS PATCH Y DELETE (MODIFICACIÓN) - SEMANA 2 (APARTADO D)
    // ========================================================================================

    /**
     * Modifica la fecha de una reserva futura.
     * Requisito: D.1 de Semana 2 - "Modificar la fecha... solo si la embarcación está disponible"[cite: 102].
     */
    @PatchMapping("/{id}/fecha")
    public ResponseEntity<Reserva> updateReservaFecha(@PathVariable Integer id, @RequestBody Reserva datosNuevos) {
        try {
            // Buscamos la reserva original.
            Reserva reservaExistente = reservaRepository.findReservaById(id);
            if (reservaExistente == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            LocalDate nuevaFecha = datosNuevos.getFecha();
            if (nuevaFecha == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            // VALIDACIÓN: No se puede mover una reserva al pasado.
            if (nuevaFecha.isBefore(LocalDate.now())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // VALIDACIÓN: Disponibilidad en la nueva fecha.
            // Reutilizamos el método auxiliar. Si devuelve true, es que está ocupada.
            if (isEmbarcacionOcupada(reservaExistente.getMatricula_embarcacion(), nuevaFecha)) {
                return new ResponseEntity<>(HttpStatus.CONFLICT); // El barco ya está cogido ese día.
            }

            // Aplicamos el cambio y guardamos.
            reservaExistente.setFecha(nuevaFecha);
            boolean exito = reservaRepository.updateReserva(reservaExistente);

            if (exito) return new ResponseEntity<>(reservaExistente, HttpStatus.OK);
            else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Modifica descripción y plazas de una reserva.
     * Requisito: D.2 de Semana 2 - "Comprobar que no exceda la capacidad máxima"[cite: 103, 104].
     */
    @PatchMapping("/{id}/detalles")
    public ResponseEntity<Reserva> updateReservaDetalles(@PathVariable Integer id, @RequestBody Reserva datosNuevos) {
        try {
            Reserva reservaExistente = reservaRepository.findReservaById(id);
            if (reservaExistente == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            // Si viene descripción, la actualizamos.
            if (datosNuevos.getDescripcion() != null) {
                reservaExistente.setDescripcion(datosNuevos.getDescripcion());
            }

            // Si vienen plazas, validamos capacidad antes de actualizar.
            if (datosNuevos.getPlazas() > 0) {
                Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(reservaExistente.getMatricula_embarcacion());

                // REGLA DE NEGOCIO: Plazas + 1 <= Capacidad total.
                if (embarcacion != null && (datosNuevos.getPlazas() + 1) > embarcacion.getPlazas()) {
                    return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY); // Error 422
                }

                // 1. Actualizamos las plazas
                reservaExistente.setPlazas(datosNuevos.getPlazas());

                // 2. AÑADE ESTO AQUÍ (Esto es lo que te falta):
                double precioCalculado = 40.0 * datosNuevos.getPlazas();
                reservaExistente.setPrecio(precioCalculado);
            }

            boolean exito = reservaRepository.updateReserva(reservaExistente);

            if (exito) return new ResponseEntity<>(reservaExistente, HttpStatus.OK);
            else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Cancela una reserva existente.
     * Requisito: D.3 de Semana 2 - "Cancelar una reserva... que aún no se haya realizado"[cite: 105].
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Integer id) {
        try {
            Reserva reserva = reservaRepository.findReservaById(id);
            if (reserva == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            // VALIDACIÓN: No permitir cancelar reservas que ya han pasado.
            if (reserva.getFecha().isBefore(LocalDate.now())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            boolean exito = reservaRepository.deleteReserva(id);

            if (exito) return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content es estándar para delete exitoso.
            else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ========================================================================================
    // MÉTODOS PRIVADOS AUXILIARES
    // ========================================================================================

    /**
     * Método auxiliar para comprobar si una embarcación está ocupada en una fecha específica.
     * Revisa conflictos tanto en la tabla de ALQUILERES como en la de RESERVAS.
     * * @param matricula Matrícula del barco.
     * @param fecha Fecha que queremos comprobar.
     * @return true si está ocupada, false si está libre.
     */
    private boolean isEmbarcacionOcupada(String matricula, LocalDate fecha) {
        // 1. Comprobar conflictos con ALQUILERES (Rango de fechas).
        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        if (alquileres != null) {
            for (Alquiler alquiler : alquileres) {
                if (alquiler.getMatricula_embarcacion().equals(matricula)) {
                    // Lógica: Si la fecha solicitada NO es antes del inicio Y NO es después del fin,
                    // significa que está DENTRO del periodo de alquiler.
                    if (!fecha.isBefore(alquiler.getFechainicio()) && !fecha.isAfter(alquiler.getFechafin())) {
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
                if (r.getMatricula_embarcacion().equals(matricula) && r.getFecha().equals(fecha)) {
                    return true;
                }
            }
        }
        return false; // Está libre
    }
}