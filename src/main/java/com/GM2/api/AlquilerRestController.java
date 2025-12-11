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

@RestController()
@RequestMapping(path = "api/alquileres", produces = "application/json")
public class AlquilerRestController {
    
    AlquilerRepository alquilerRepository;
    EmbarcacionRepository embarcacionRepository;
    SocioRepository socioRepository;
    ReservaRepository reservaRepository;
    AcompananteRepository acompanantesRepository;

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
     */
    @GetMapping(params = "fecha")
    public ResponseEntity<List<Alquiler>> getAlquileresFuturos(@RequestParam LocalDate fecha) {
        try {
            List<Alquiler> todosAlquileres = alquilerRepository.findAllAlquileres();
            List<Alquiler> alquileresFuturos = new ArrayList<>();

            if (todosAlquileres != null) {
                for (Alquiler alquiler : todosAlquileres) {
                    if (alquiler.getFechainicio().isAfter(fecha) || 
                        alquiler.getFechainicio().isEqual(fecha)) {
                        alquileresFuturos.add(alquiler);
                    }
                }
            }

            if (alquileresFuturos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(alquileresFuturos, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 3. Obtener la información concreta de un alquiler dado su identificador (GET)
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

                // Verificar conflictos con alquileres existentes
                if (todosAlquileres != null) {
                    for (Alquiler alquiler : todosAlquileres) {
                        if (alquiler.getMatricula_embarcacion().equals(embarcacion.getMatricula())) {
                            // Verificar superposición de fechas
                            if (!(fechaFin.isBefore(alquiler.getFechainicio()) || fechaInicio.isAfter(alquiler.getFechafin()))) {
                                disponible = false;
                                break;
                            }
                        }
                    }
                }

                if(disponible) {
                    // Verificar conflictos con reservas existentes
                    if (todosReservas != null) {
                        for (Reserva reserva : todosReservas) {
                            if (reserva.getMatricula_embarcacion().equals(embarcacion.getMatricula())) {
                                // Verificar superposición de fechas
                                if (!(fechaFin.isBefore(reserva.getFecha()) || fechaInicio.isAfter(reserva.getFecha()))) {
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
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Alquiler> createAlquiler(@RequestBody Alquiler nuevoAlquiler) {
        try {

            //Verificar datos del alquiler no nulos
            if (nuevoAlquiler.getFechainicio() == null || nuevoAlquiler.getFechafin() == null ||
                nuevoAlquiler.getPlazas() <= 0 ||
                nuevoAlquiler.getUsuario_dni() == null || nuevoAlquiler.getUsuario_dni().trim().isEmpty() ||
                nuevoAlquiler.getMatricula_embarcacion() == null || nuevoAlquiler.getMatricula_embarcacion().trim().isEmpty()) {
                
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
             }


            // Validaciones básicas
            if (nuevoAlquiler.getFechainicio().isAfter(nuevoAlquiler.getFechafin())) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            Socio socioAlquiler = socioRepository.findSocioByDNI(nuevoAlquiler.getUsuario_dni());
            // Verificar que el socio existe
            if ( socioAlquiler == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            //Verificar que el socio tenga título de patrón
            if (!socioAlquiler.getTieneLicenciaPatron() ){
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            // Verificar que la embarcación existe
            Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(nuevoAlquiler.getMatricula_embarcacion());
            if (embarcacion == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            // Verificar que no excede las plazas disponibles
            if (nuevoAlquiler.getPlazas() > embarcacion.getPlazas()) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            // Verificar disponibilidad de la embarcación en las fechas solicitadas
            List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
            List<Reserva> reservas = reservaRepository.findAllReservas();

            for (Alquiler alquiler : alquileres) {
                if (alquiler.getMatricula_embarcacion().equals(nuevoAlquiler.getMatricula_embarcacion())) {
                    // Verificar superposición de fechas
                    if (!(nuevoAlquiler.getFechafin().isBefore(alquiler.getFechainicio()) || nuevoAlquiler.getFechainicio().isAfter(alquiler.getFechafin()))) {
                        return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                }
            }

            for (Reserva reserva : reservas) {
                if (reserva.getMatricula_embarcacion().equals(nuevoAlquiler.getMatricula_embarcacion())) {
                    // Verificar superposición de fechas
                    if (!(nuevoAlquiler.getFechafin().isBefore(reserva.getFecha()) || nuevoAlquiler.getFechainicio().isAfter(reserva.getFecha()))) {
                        return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                }
            }   

            // Calcular precio (20€ por plaza por día)
            long dias = ChronoUnit.DAYS.between(nuevoAlquiler.getFechainicio(), nuevoAlquiler.getFechafin()) + 1;
            double precio = 20.0 * nuevoAlquiler.getPlazas() * dias;
            nuevoAlquiler.setPrecio(precio);

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


    @PatchMapping("/{id}/acompanantes")
    public ResponseEntity<Alquiler> addAcompanante(@PathVariable Integer id, @RequestBody String dniSocio) {
        try {
            // Verificar que el alquiler existe y es futuro
            Alquiler alquiler = alquilerRepository.findAlquilerById(id);
            if (alquiler == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            if (alquiler.getFechainicio().isBefore(LocalDate.now())) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            // Verificar que el socio existe
            Socio socio = socioRepository.findSocioByDNI(dniSocio);
            if (socio == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }


            // Verificar que no es el socio titular
            if (dniSocio.equals(alquiler.getUsuario_dni())) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            // Verificar que el socio no está ya vinculado
            List<Acompanante> acompanantes = acompanantesRepository.findAcompananteByAlquiler(id);
            for (Acompanante acompanante : acompanantes) {
                if (acompanante.getDni().equals(dniSocio)) {
                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                }
            }


            // Verificar que hay plazas disponibles
            int plazasOcupadas = acompanantes.size() + 1; // +1 por el titular
            if (plazasOcupadas > alquiler.getPlazas()) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            // Añadir acompañante
            Acompanante acompanante = new Acompanante();
            acompanante.setDni(dniSocio);
            acompanante.setId_alquiler(id);

            boolean exito = acompanantesRepository.addAcompanante(acompanante);
            if (!exito) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

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

    @PatchMapping("/{id}/acompanantes/{dniSocio}")
    public ResponseEntity<Alquiler> removeAcompanante(@PathVariable Integer id, 
                                                     @PathVariable String dniSocio) {
        try {
            // Verificar que el alquiler existe y es futuro
            Alquiler alquiler = alquilerRepository.findAlquilerById(id);
            if (alquiler == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            if (alquiler.getFechainicio().isBefore(LocalDate.now())) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            boolean encontrado = false;
            // Verificar que el socio está vinculado como acompañante
            List<Acompanante> acompanantes = acompanantesRepository.findAcompananteByAlquiler(id);
            for (Acompanante acompanante : acompanantes) {
                if (acompanante.getDni().equals(dniSocio)) {
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


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAlquiler(@PathVariable Integer id) {
        try {
            // Verificar que el alquiler existe
            Alquiler alquiler = alquilerRepository.findAlquilerById(id);
            if (alquiler == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Verificar que es futuro
            if (alquiler.getFechainicio().isBefore(LocalDate.now())) {
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

}