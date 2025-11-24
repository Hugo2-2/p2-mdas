package com.GM2.api; // Asegúrate de que este paquete sea correcto en tu proyecto

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
 * Implementa el APARTADO D de la práctica (Semana 1).
 */
@RestController() // Indica que esta clase maneja peticiones REST y devuelve JSON directamente (no HTML).
@RequestMapping(path = "api/reservas", produces = "application/json") // Define la URL base: localhost:8080/api/reservas
public class ReservaRestController {

    // Declaramos los repositorios necesarios.
    // Necesitamos acceso a Reservas, Embarcaciones (para capacidad) y Alquileres (para disponibilidad).
    ReservaRepository reservaRepository;
    EmbarcacionRepository embarcacionRepository;
    AlquilerRepository alquilerRepository;

    // Constructor para Inyección de Dependencias. Spring nos dará las instancias de los repositorios automáticamente.
    public ReservaRestController(ReservaRepository reservaRepository,
                                 EmbarcacionRepository embarcacionRepository,
                                 AlquilerRepository alquilerRepository) {
        this.reservaRepository = reservaRepository;
        this.embarcacionRepository = embarcacionRepository;
        this.alquilerRepository = alquilerRepository;

        // Configuración manual del archivo de propiedades SQL (Requisito de tu arquitectura actual)
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.reservaRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.alquilerRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * REQUISITO D.1: Obtener la lista completa de reservas.
     * Método HTTP: GET
     * URL: /api/reservas
     */
    @GetMapping
    public ResponseEntity<List<Reserva>> getAllReservas() {
        try {
            // Llamamos al repositorio para traer todo lo que hay en la tabla Reservas
            List<Reserva> reservas = reservaRepository.findAllReservas();

            // Si la lista es nula o está vacía, devolvemos un 204 (No Content) para indicar que no hay datos.
            if (reservas == null || reservas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            // Si hay datos, devolvemos la lista y un 200 (OK).
            return new ResponseEntity<>(reservas, HttpStatus.OK);

        } catch (Exception e) {
            // Si falla la base de datos, devolvemos un 500 (Error Interno).
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * REQUISITO D.2: Obtener la lista de reservas futuras dada una fecha.
     * Método HTTP: GET
     * URL Ejemplo: /api/reservas?fecha=2025-10-10
     * Nota: Usamos @RequestParam para leer "?fecha=..." de la URL.
     */
    @GetMapping(params = "fecha")
    public ResponseEntity<List<Reserva>> getReservasFuturas(@RequestParam LocalDate fecha) {
        try {
            // 1. Obtenemos TODAS las reservas (porque el repositorio no tiene un método SQL específico para filtrar por fecha).
            List<Reserva> todasReservas = reservaRepository.findAllReservas();
            List<Reserva> reservasFuturas = new ArrayList<>();

            // 2. Filtramos manualmente en Java:
            if (todasReservas != null) {
                for (Reserva reserva : todasReservas) {
                    // Si la fecha de la reserva es posterior a la fecha del parámetro...
                    if (reserva.getFecha().isAfter(fecha)) {
                        reservasFuturas.add(reserva); // ... la añadimos a la lista de resultados.
                    }
                }
            }

            // Si tras filtrar no queda ninguna, devolvemos 204 No Content.
            if (reservasFuturas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            // Devolvemos las filtradas con 200 OK.
            return new ResponseEntity<>(reservasFuturas, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * REQUISITO D.3: Obtener la información concreta de una reserva por ID.
     * Método HTTP: GET
     * URL Ejemplo: /api/reservas/5
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable Integer id) {
        try {
            // Buscamos por ID usando el repositorio
            Reserva reserva = reservaRepository.findReservaById(id);

            // Si es null, significa que ese ID no existe -> Devolvemos 404 Not Found.
            if (reserva == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Si existe, devolvemos el objeto y 200 OK.
            return new ResponseEntity<>(reserva, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * REQUISITO D.4: Crear una reserva para una embarcación disponible.
     * Método HTTP: POST
     * Body: JSON con los datos de la reserva.
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Reserva> createReserva(@RequestBody Reserva nuevaReserva) {
        try {
            // -----------------------------------------------------------------
            // PASO 1: VALIDACIÓN DE INTEGRIDAD (¿Existen los datos base?)
            // -----------------------------------------------------------------

            // Buscamos la embarcación indicada en la reserva
            Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(nuevaReserva.getMatricula_embarcacion());

            // Si la embarcación no existe, no podemos reservar -> 422 Unprocessable Entity
            if (embarcacion == null) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            // Validamos Capacidad: Plazas solicitadas + 1 (Patrón) <= Plazas totales del barco
            if ((nuevaReserva.getPlazas() + 1) > embarcacion.getPlazas()) {
                // Si intentan meter más gente de la que cabe -> 422 Unprocessable Entity
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            // -----------------------------------------------------------------
            // PASO 2: VALIDACIÓN DE DISPONIBILIDAD (¿Está libre el barco?)
            // -----------------------------------------------------------------

            LocalDate fechaSolicitada = nuevaReserva.getFecha();
            boolean ocupada = false; // Asumimos que está libre hasta que demostremos lo contrario

            // A) Comprobar conflictos con ALQUILERES (que duran varios días)
            List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
            if (alquileres != null) {
                for (Alquiler alquiler : alquileres) {
                    // Si es el mismo barco...
                    if (alquiler.getMatricula_embarcacion().equals(nuevaReserva.getMatricula_embarcacion())) {
                        // ... comprobamos si la fecha solicitada cae DENTRO del rango del alquiler (Inicio <= Fecha <= Fin)
                        // La lógica inversa es: NO es válida si NO es antes del inicio Y NO es después del fin.
                        if (!fechaSolicitada.isBefore(alquiler.getFechainicio()) &&
                                !fechaSolicitada.isAfter(alquiler.getFechafin())) {
                            ocupada = true; // Conflicto encontrado
                            break;
                        }
                    }
                }
            }

            // B) Comprobar conflictos con OTRAS RESERVAS (que son de 1 día)
            // Solo comprobamos esto si no hemos encontrado conflicto con alquileres aún
            if (!ocupada) {
                List<Reserva> reservas = reservaRepository.findAllReservas();
                if (reservas != null) {
                    for (Reserva r : reservas) {
                        // Si es el mismo barco Y es exactamente el mismo día...
                        if (r.getMatricula_embarcacion().equals(nuevaReserva.getMatricula_embarcacion()) &&
                                r.getFecha().equals(fechaSolicitada)) {
                            ocupada = true; // Conflicto encontrado
                            break;
                        }
                    }
                }
            }

            // Si tras comprobar todo, la variable 'ocupada' es true, devolvemos error.
            if (ocupada) {
                // 409 Conflict: El recurso está bloqueado/ocupado por otra entidad.
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            // -----------------------------------------------------------------
            // PASO 3: FINALIZACIÓN Y GUARDADO
            // -----------------------------------------------------------------

            // Calculamos el precio automáticamente si el JSON viene con precio 0.
            // Regla de negocio simple: 40.0 euros por plaza reservada.
            if (nuevaReserva.getPrecio() == 0) {
                double precioCalculado = 40.0 * nuevaReserva.getPlazas();
                nuevaReserva.setPrecio(precioCalculado);
            }

            // Guardamos en la base de datos
            boolean exito = reservaRepository.addReserva(nuevaReserva);

            if (exito) {
                // Si guardó bien, devolvemos el objeto creado y un 201 Created.
                return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
            } else {
                // Si el repositorio devuelve false, algo interno falló -> 500 Internal Server Error.
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            // Captura de excepciones generales para que el servidor no se caiga
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}