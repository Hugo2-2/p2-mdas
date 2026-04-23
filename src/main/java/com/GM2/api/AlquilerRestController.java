package com.GM2.api;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador REST de la API de alquileres.
 * Permite realizar operaciones CRUD sobre alquileres.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
@RestController()
@RequestMapping(path = "api/alquileres", produces = "application/json")
public class AlquilerRestController {
    
    AlquilerRepository alquilerRepository;
    EmbarcacionRepository embarcacionRepository;
    SocioRepository socioRepository;
    ReservaRepository reservaRepository;
    AcompananteRepository acompanantesRepository;

    /**
     * Constructor de la clase.
     * 
     * @param alquilerRepository Instancia de AlquilerRepository
     * @param embarcacionRepository Instancia de EmbarcacionRepository
     * @param socioRepository Instancia de SocioRepository
     * @param reservaRepository Instancia de ReservaRepository
     * @param acompanantesRepository Instancia de AcompananteRepository
     */
    public AlquilerRestController(AlquilerRepository alquilerRepository, 
                                 EmbarcacionRepository embarcacionRepository,
                                 SocioRepository socioRepository,
                                 ReservaRepository reservaRepository,
                                 AcompananteRepository acompanantesRepository) {
        this.alquilerRepository = alquilerRepository;
        this.embarcacionRepository = embarcacionRepository;
        this.socioRepository = socioRepository;
        this.reservaRepository = reservaRepository;
        this.acompanantesRepository = acompanantesRepository;
        
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.alquilerRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.reservaRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.acompanantesRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * 1. Obtener la lista completa de alquileres (GET)
     * 
     * @return ResponseEntity con la lista de alquileres y estado 200 (OK) si se ha podido obtener correctamente
     * y en caso de error: 404 (Not Found) o 500 (Internal Server Error)
     */
    @GetMapping
    public ResponseEntity<List<Alquiler>> getAllAlquileres() {
        try {
            List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();

            if (alquileres == null || alquileres.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(alquileres, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 2. Obtener la lista de alquileres futuros dada una fecha (GET)
     * 
     * @param fecha Fecha de inicio de los alquileres futuros
     * @return ResponseEntity con la lista de alquileres futuros y estado 200 (OK) si se ha podido obtener correctamente
     * y en caso de error: 404 (Not Found) o 500 (Internal Server Error)
     */
    @GetMapping(params = "fecha")
    public ResponseEntity<List<Alquiler>> getAlquileresFuturos(@RequestParam LocalDate fecha) {
        try {
            List<Alquiler> todosAlquileres = alquilerRepository.findAllAlquileres();
            List<Alquiler> alquileresFuturos = new ArrayList<>();

            if (todosAlquileres != null) {
                for (Alquiler alquiler : todosAlquileres) {
                    if (alquiler.getStartDate().isAfter(fecha) || 
                        alquiler.getStartDate().isEqual(fecha)) {
                        alquileresFuturos.add(alquiler);
                    }
                }
            }

            if (alquileresFuturos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(alquileresFuturos, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 3. Obtener la información concreta de un alquiler dado su identificador (GET)
     * 
     * @param id Identificador del alquiler
     * @return ResponseEntity con la información del alquiler y estado 200 (OK) si se ha podido obtener correctamente
     * y en caso de error: 404 (Not Found) o 500 (Internal Server Error)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Alquiler> getAlquilerById(@PathVariable Integer id) {
        try {
            Alquiler alquiler = alquilerRepository.findAlquilerById(id);

            if (alquiler == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(alquiler, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 4. Obtener las embarcaciones disponibles dada una fecha de inicio y de fin (GET)
     * 
     * @param fechaInicio Fecha de inicio de las embarcaciones
     * @param fechaFin Fecha de fin de las embarcaciones
     * @return ResponseEntity con la lista de embarcaciones disponibles y estado 200 (OK) o 204 (No Content) si no se ha podido obtener ninguna embarcación
     * Tambien en caso de error 500 (Internal Server Error)
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<Embarcacion>> getEmbarcacionesDisponibles(
            @RequestParam LocalDate fechaInicio, 
            @RequestParam LocalDate fechaFin) {
        try {
            // Validar fechas
            if (fechaInicio.isAfter(fechaFin)) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            List<Embarcacion> todasEmbarcaciones = embarcacionRepository.findAllEmbarcaciones();
            List<Alquiler> todosAlquileres = alquilerRepository.findAllAlquileres();
            List<Reserva> todosReservas = reservaRepository.findAllReservas();
            List<Embarcacion> embarcacionesDisponibles = new ArrayList<>();

            if (todasEmbarcaciones == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            for (Embarcacion embarcacion : todasEmbarcaciones) {
                boolean disponible = true;

                // Clean Code - Reglas de comentarios: Comentario que explica intención del código
                if (todosAlquileres != null) {
                    for (Alquiler alquiler : todosAlquileres) {
                        if (alquiler.getBoatRegistration().equals(embarcacion.getRegistration())) {
                            // Clean Code - Regla 6: Se ha eliminado el comentario explicativo y extraído la condicional compleja a la función 'haySuperposicionConAlquiler'.
                            if (haySuperposicionConAlquiler(fechaInicio, fechaFin, alquiler)) {
                                disponible = false;
                                break;
                            }
                        }
                    }
                }

                if (disponible) {
                    if (todosReservas != null) {
                        for (Reserva reserva : todosReservas) {
                            if (reserva.getBoatRegistration().equals(embarcacion.getRegistration())) {
                                // Clean Code - Regla 6: Se ha eliminado el comentario explicativo y extraído la condicional compleja a la función 'haySuperposicionConReserva'.
                                if (haySuperposicionConReserva(fechaInicio, fechaFin, reserva)) {
                                    disponible = false;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (disponible) {
                    embarcacionesDisponibles.add(embarcacion);
                }
            }

            if (embarcacionesDisponibles.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(embarcacionesDisponibles, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 5. Crear un alquiler para una embarcación disponible, sin incluir la vinculación de 
     *    los socios que participan en ella (POST)
     * 
     * @param nuevoAlquiler Alquiler a crear
     * @return ResponseEntity con el alquiler creado y estado 200 OK si se ha podido crear correctamente
     * y en caso de error: 400 (Bad Request), 404 (Not Found), 409 (Conflict), 422 (Unprocessable Entity) o 500 (Internal Server Error)
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Alquiler> createAlquiler(@RequestBody Alquiler nuevoAlquiler) {
        try {

            // Clean Code - Regla 6: Se ha eliminado el comentario explicativo y extraído la condicional compleja a la función 'tieneCamposObligatoriosNulos'.
            if (tieneCamposObligatoriosNulos(nuevoAlquiler)) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }


            // Validaciones básicas
            if (nuevoAlquiler.getStartDate().isAfter(nuevoAlquiler.getEndDate())) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            LocalDate inicio = nuevoAlquiler.getStartDate();
            LocalDate fin = nuevoAlquiler.getEndDate();
            // Clean Code - Reglas de nombrado: variable con unidad (dias -> totalDays)
            long totalDays = ChronoUnit.DAYS.between(inicio, fin) + 1;
            
            int mesInicio = inicio.getMonthValue();
            
            if (mesInicio >= 10 || mesInicio <= 4) {
                if (totalDays > 3) {
                    return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            } 
            else if (mesInicio >= 5 && mesInicio <= 9) {
                if (totalDays != 7 && totalDays != 14) {
                    return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }


            Socio socioAlquiler = socioRepository.findSocioByDNI(nuevoAlquiler.getUserNationalId());
            // Verificar que el socio existe
            if ( socioAlquiler == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            //Verificar que el socio tenga título de patrón
            if (!socioAlquiler.getHasSkipperLicense() ){
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            // Verificar que la embarcación existe
            Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(nuevoAlquiler.getBoatRegistration());
            if (embarcacion == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            // Verificar disponibilidad de la embarcación en las fechas solicitadas
            List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
            List<Reserva> reservas = reservaRepository.findAllReservas();

            for (Alquiler alquiler : alquileres) {
                if (alquiler.getBoatRegistration().equals(nuevoAlquiler.getBoatRegistration())) {
                    // Clean Code - Regla 6: Se ha eliminado el comentario explicativo y extraído la condicional compleja a la función 'haySuperposicionConAlquiler'.
                    if (haySuperposicionConAlquiler(nuevoAlquiler.getStartDate(), nuevoAlquiler.getEndDate(), alquiler)) {
                        return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                }
            }

            for (Reserva reserva : reservas) {
                if (reserva.getBoatRegistration().equals(nuevoAlquiler.getBoatRegistration())) {
                    // Clean Code - Regla 6: Se ha eliminado el comentario explicativo y extraído la condicional compleja a la función 'haySuperposicionConReserva'.
                    if (haySuperposicionConReserva(nuevoAlquiler.getStartDate(), nuevoAlquiler.getEndDate(), reserva)) {
                        return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                }
            }   

            // Calcular precio inicial (20€ por día para el creador del alquiler)

            // Clean Code - Reglas de nombrado: variable con unidad (precio -> priceInEuros )
            double priceInEuros = 20.0 * totalDays;
            nuevoAlquiler.setPrice(priceInEuros);
            nuevoAlquiler.setSeats(1);

            // Crear el alquiler
            boolean exito = alquilerRepository.addAlquiler(nuevoAlquiler);

            if (exito) {
                return new ResponseEntity<>(nuevoAlquiler, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 6. Vincular a un nuevo socio (no titular) a un alquiler futuro, actualizando el coste y el número de plazas reservadas (PATCH)
     * 
     * @param id Identificador del alquiler
     * @param dniSocio DNI del acompanante
     * @return ResponseEntity con el alquiler actualizado y estado 200 (OK) si se ha podido actualizar correctamente
     * y en caso de error: 404 (Not Found), 409 (Conflict), 422 (Unprocessable Entity) o 500 (Internal Server Error)
     */
    @PatchMapping("/{id}/acompanantes")
    public ResponseEntity<Alquiler> addAcompanante(@PathVariable Integer id, @RequestBody String dniSocio) {
        try {
            // Verificar que el alquiler existe y es futuro
            Alquiler alquiler = alquilerRepository.findAlquilerById(id);
            if (alquiler == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            if (alquiler.getStartDate().isBefore(LocalDate.now())) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            // Verificar que el socio existe
            Socio socio = socioRepository.findSocioByDNI(dniSocio);
            if (socio == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            // Verificar que no es el socio titular
            if (dniSocio.equals(alquiler.getUserNationalId())) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            // Verificar que el socio no está ya vinculado
            List<Acompanante> acompanantes = acompanantesRepository.findAcompananteByAlquiler(id);
            for (Acompanante acompanante : acompanantes) {
                if (acompanante.getNationalId().equals(dniSocio)) {
                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                }
            }

            // Obtener embarcación para verificar límite de plazas
            Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(alquiler.getBoatRegistration());
            if (embarcacion == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            // Verificar que hay plazas disponibles en la embarcación
            int totalPersonas = acompanantes.size() + 1; // +1 por el titular
            if (totalPersonas >= embarcacion.getSeats()) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            // Añadir acompañante
            Acompanante acompanante = new Acompanante();
            acompanante.setNationalId(dniSocio);
            acompanante.setRentalId(id);

            boolean exito = acompanantesRepository.addAcompanante(acompanante);
            if (!exito) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Actualizar número de plazas (1 titular + acompañantes)
            int nuevasPlazas = totalPersonas + 1; // Añadimos el nuevo acompañante
            alquiler.setSeats(nuevasPlazas);
            
            // Recalcular precio

            // Clean Code - Reglas de nombrado: variable con unidad (dias -> totalDays)
            long totalDays = ChronoUnit.DAYS.between(alquiler.getStartDate(), alquiler.getEndDate()) + 1;
            // Clean Code - Reglas de nombrado: variable con unidad (nuveoPrecio -> newPriceInEuros )
            double newPriceInEuros = 20.0 * alquiler.getSeats() * totalDays;
            alquiler.setPrice(newPriceInEuros);

            boolean actualizado = alquilerRepository.updateAlquiler(alquiler);
            if (!actualizado) {
                // Si falla la actualización, revertir la adición del acompañante
                acompanantesRepository.deleteAcompanante(id, dniSocio);
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            alquiler = alquilerRepository.findAlquilerById(id);
            if (alquiler == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(alquiler, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 7. Desvincular a un socio (no titular) de un alquiler futuro, actualizando el coste y el número de plazas reservadas (PATCH)
     * 
     * @param id Identificador del alquiler
     * @param dniSocio DNI del acompanante
     * 
     * @return ResponseEntity con el alquiler actualizado y estado 200 (OK) si se ha podido actualizar correctamente
     * y en caso de error: 404 (Not Found), 409 (Conflict), 422 (Unprocessable Entity) o 500 (Internal Server Error)
     */
    @PatchMapping("/{id}/acompanantes/{dniSocio}")
    public ResponseEntity<Alquiler> removeAcompanante(@PathVariable Integer id, 
                                                    @PathVariable String dniSocio) {
        try {
            // Verificar que el alquiler existe y es futuro
            Alquiler alquiler = alquilerRepository.findAlquilerById(id);
            if (alquiler == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            if (alquiler.getStartDate().isBefore(LocalDate.now())) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            // Verificar que no se intenta eliminar al titular
            if (dniSocio.equals(alquiler.getUserNationalId())) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            boolean encontrado = false;
            // Verificar que el socio está vinculado como acompañante
            List<Acompanante> acompanantes = acompanantesRepository.findAcompananteByAlquiler(id);
            for (Acompanante acompanante : acompanantes) {
                if (acompanante.getNationalId().equals(dniSocio)) {
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            // Eliminar acompañante
            boolean exito = acompanantesRepository.deleteAcompanante(id, dniSocio);
            if (!exito) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Obtener lista actualizada de acompañantes
            acompanantes = acompanantesRepository.findAcompananteByAlquiler(id);
            // Actualizar plazas (1 titular + acompañantes restantes)
            alquiler.setSeats(acompanantes.size() + 1);

            // Recalcular precio
            // Clean Code - Reglas de nombrado: variable con unidad (dias -> totalDays)
            long totalDays = ChronoUnit.DAYS.between(alquiler.getStartDate(), alquiler.getEndDate()) + 1;
            double nuevoPrecio = 20.0 * alquiler.getSeats() * totalDays;
            alquiler.setPrice(nuevoPrecio);

            boolean actualizado = alquilerRepository.updateAlquiler(alquiler);
            if (!actualizado) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            alquiler = alquilerRepository.findAlquilerById(id);
            if (alquiler == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(alquiler, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 8. Cancelar un alquiler que aún no se haya realizado (DELETE)
     * 
     * @param id Identificador del alquiler
     * 
     * @return ResponseEntity con estado 204 (No Content) si se ha podido cancelar correctamente
     * y en caso de error: 404 (Not Found), 422 (Unprocessable Entity) o 500 (Internal Server Error)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAlquiler(@PathVariable Integer id) {
        try {
            // Verificar que el alquiler existe
            Alquiler alquiler = alquilerRepository.findAlquilerById(id);
            if (alquiler == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Verificar que es futuro
            if (alquiler.getStartDate().isBefore(LocalDate.now())) {
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }

            // Eliminar el alquiler
            boolean exito = alquilerRepository.deleteAlquiler(id);
            if (exito) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Clean Code - Regla 7: Se han eliminado los marcadores de separación visuales (ej. --- Métodos privados ---) para evitar ruido innecesario en el código.
    /**
     * Verifica si los campos obligatorios de un alquiler son nulos o están vacíos.
     *
     * @param alquiler Alquiler a validar
     * @return true si alguno de los campos obligatorios es nulo o está vacío
     */
    private boolean tieneCamposObligatoriosNulos(Alquiler alquiler) {
        return alquiler.getStartDate() == null || alquiler.getEndDate() == null ||
               alquiler.getUserNationalId() == null || alquiler.getUserNationalId().trim().isEmpty() ||
               alquiler.getBoatRegistration() == null || alquiler.getBoatRegistration().trim().isEmpty();
    }

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
        return !(fin.isBefore(alquilerExistente.getStartDate()) || inicio.isAfter(alquilerExistente.getEndDate()));
    }

    /**
     * Verifica si un rango de fechas [inicio, fin] se superpone con la fecha de una reserva existente.
     * Una reserva ocupa un único día, por lo que hay superposición si ese día cae dentro del rango.
     *
     * @param inicio           Fecha de inicio del nuevo período
     * @param fin              Fecha de fin del nuevo período
     * @param reservaExistente Reserva con la que se compara
     * @return true si existe superposición de fechas
     */
    private boolean haySuperposicionConReserva(LocalDate inicio, LocalDate fin, Reserva reservaExistente) {
        return !(fin.isBefore(reservaExistente.getDate()) || inicio.isAfter(reservaExistente.getDate()));
    }

}