package com.GM2.api;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

/**
 * Controlador REST de la API de alquileres.
 * Permite realizar operaciones CRUD sobre alquileres.
 *
 * Refactoring 2.3: Eliminada configuración de SQL path duplicada en constructor
 * (ahora se centraliza en {@link com.GM2.model.repository.AbstractRepository}).
 *
 * Refactoring 1.1: Extraídos métodos de validación para mejorar legibilidad.
 * Refactoring 4.1: Descompuestos condicionales complejos en métodos con nombre descriptivo.
 * Refactoring 3.1: Uso de constantes para cálculo de precios.
 *
 * @author gm2equipo1
 * @version 1.0
 */
@RestController
@RequestMapping(path = "api/alquileres", produces = "application/json")
public class AlquilerRestController {

    /** Máximo de días permitidos en temporada baja (octubre–abril). */
    private static final int MAX_DAYS_LOW_SEASON = 3;

    /** Duración permitida en temporada alta: 7 o 14 días (mayo–septiembre). */
    private static final int WEEK = 7;
    private static final int TWO_WEEKS = 14;

    private final AlquilerRepository alquilerRepository;
    private final EmbarcacionRepository embarcacionRepository;
    private final SocioRepository socioRepository;
    private final ReservaRepository reservaRepository;
    private final AcompananteRepository acompanantesRepository;

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
    }

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

    @GetMapping(params = "fecha")
    public ResponseEntity<List<Alquiler>> getAlquileresFuturos(@RequestParam LocalDate fecha) {
        try {
            return filtrarAlquileresFuturos(fecha);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<List<Alquiler>> filtrarAlquileresFuturos(LocalDate fecha) {
        List<Alquiler> todosAlquileres = alquilerRepository.findAllAlquileres();
        List<Alquiler> alquileresFuturos = new ArrayList<>();

        if (todosAlquileres != null) {
            for (Alquiler alquiler : todosAlquileres) {
                if (!alquiler.getStartDate().isBefore(fecha)) {
                    alquileresFuturos.add(alquiler);
                }
            }
        }

        if (alquileresFuturos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(alquileresFuturos, HttpStatus.OK);
    }

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

    @GetMapping("/disponibles")
    public ResponseEntity<List<Embarcacion>> getEmbarcacionesDisponibles(
            @RequestParam LocalDate fechaInicio,
            @RequestParam LocalDate fechaFin) {
        try {
            return buscarEmbarcacionesDisponibles(fechaInicio, fechaFin);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<List<Embarcacion>> buscarEmbarcacionesDisponibles(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio.isAfter(fechaFin)) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        List<Embarcacion> todasEmbarcaciones = embarcacionRepository.findAllEmbarcaciones();
        if (todasEmbarcaciones == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<Alquiler> todosAlquileres = alquilerRepository.findAllAlquileres();
        List<Reserva> todasReservas = reservaRepository.findAllReservas();
        List<Embarcacion> embarcacionesDisponibles = new ArrayList<>();

        for (Embarcacion embarcacion : todasEmbarcaciones) {
            boolean disponible = isEmbarcacionDisponible(embarcacion, fechaInicio, fechaFin, todosAlquileres, todasReservas);
            if (disponible) {
                embarcacionesDisponibles.add(embarcacion);
            }
        }

        if (embarcacionesDisponibles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(embarcacionesDisponibles, HttpStatus.OK);
    }

    /**
     * Refactoring 1.1: Extract Method — verifica si una embarcación está disponible
     * comprobando conflictos con alquileres y reservas existentes.
     */
    private boolean isEmbarcacionDisponible(Embarcacion embarcacion, LocalDate fechaInicio, LocalDate fechaFin,
                                            List<Alquiler> alquileres, List<Reserva> reservas) {
        if (alquileres != null) {
            for (Alquiler alquiler : alquileres) {
                if (alquiler.getBoatRegistration().equals(embarcacion.getRegistration())
                        && haySuperposicionConAlquiler(fechaInicio, fechaFin, alquiler)) {
                    return false;
                }
            }
        }
        if (reservas != null) {
            for (Reserva reserva : reservas) {
                if (reserva.getBoatRegistration().equals(embarcacion.getRegistration())
                        && haySuperposicionConReserva(fechaInicio, fechaFin, reserva)) {
                    return false;
                }
            }
        }
        return true;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Alquiler> createAlquiler(@RequestBody Alquiler nuevoAlquiler) {
        try {
            return procesarCreacionAlquiler(nuevoAlquiler);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Refactoring 1.1: Extract Method — se han extraído validaciones individuales
     * (campos obligatorios, duración, socio con licencia, disponibilidad de embarcación)
     * para reducir la complejidad ciclomática del método principal.
     */
    private ResponseEntity<Alquiler> procesarCreacionAlquiler(Alquiler nuevoAlquiler) {
        if (tieneCamposObligatoriosNulos(nuevoAlquiler)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (nuevoAlquiler.getStartDate().isAfter(nuevoAlquiler.getEndDate())) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (!esDuracionValida(nuevoAlquiler.getStartDate(), nuevoAlquiler.getEndDate())) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Socio socioAlquiler = validarSocioConLicencia(nuevoAlquiler.getUserNationalId());
        if (socioAlquiler == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        if (!socioAlquiler.getHasSkipperLicense()) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(nuevoAlquiler.getBoatRegistration());
        if (embarcacion == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (!isEmbarcacionDisponibleParaAlquiler(nuevoAlquiler)) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        long totalDays = calcularDiasTotales(nuevoAlquiler.getStartDate(), nuevoAlquiler.getEndDate());
        nuevoAlquiler.setPrice(Alquiler.PRICE_PER_PERSON_PER_DAY * totalDays);
        nuevoAlquiler.setSeats(1);

        boolean exito = alquilerRepository.addAlquiler(nuevoAlquiler);
        if (exito) {
            return new ResponseEntity<>(nuevoAlquiler, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Refactoring 1.1: Extract Method — valida que el socio exista.
     */
    private Socio validarSocioConLicencia(String dni) {
        return socioRepository.findSocioByDNI(dni);
    }

    /**
     * Refactoring 1.1: Extract Method — verifica disponibilidad de embarcación contra
     * todos los alquileres y reservas existentes.
     */
    private boolean isEmbarcacionDisponibleParaAlquiler(Alquiler nuevoAlquiler) {
        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        List<Reserva> reservas = reservaRepository.findAllReservas();

        for (Alquiler alquiler : alquileres) {
            if (alquiler.getBoatRegistration().equals(nuevoAlquiler.getBoatRegistration())
                    && haySuperposicionConAlquiler(nuevoAlquiler.getStartDate(), nuevoAlquiler.getEndDate(), alquiler)) {
                return false;
            }
        }

        for (Reserva reserva : reservas) {
            if (reserva.getBoatRegistration().equals(nuevoAlquiler.getBoatRegistration())
                    && haySuperposicionConReserva(nuevoAlquiler.getStartDate(), nuevoAlquiler.getEndDate(), reserva)) {
                return false;
            }
        }
        return true;
    }

    @PatchMapping("/{id}/acompanantes")
    public ResponseEntity<Alquiler> addAcompanante(@PathVariable Integer id, @RequestBody String dniSocio) {
        try {
            return procesarAddAcompanante(id, dniSocio);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Alquiler> procesarAddAcompanante(Integer id, String dniSocio) {
        Alquiler alquiler = alquilerRepository.findAlquilerById(id);
        if (alquiler == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        if (alquiler.getStartDate().isBefore(LocalDate.now())) return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);

        Socio socio = socioRepository.findSocioByDNI(dniSocio);
        if (socio == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        if (dniSocio.equals(alquiler.getUserNationalId())) return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        List<Acompanante> acompanantes = acompanantesRepository.findAcompananteByAlquiler(id);
        boolean yaEsAcompanante = acompanantes.stream().anyMatch(a -> a.getNationalId().equals(dniSocio));
        if (yaEsAcompanante) return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(alquiler.getBoatRegistration());
        if (embarcacion == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        int totalPersonas = acompanantes.size() + 1;
        if (totalPersonas >= embarcacion.getSeats()) return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        Acompanante acompanante = new Acompanante();
        acompanante.setNationalId(dniSocio);
        acompanante.setRentalId(id);

        if (!acompanantesRepository.addAcompanante(acompanante)) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        int nuevasPlazas = totalPersonas + 1;
        alquiler.setSeats(nuevasPlazas);

        long totalDays = ChronoUnit.DAYS.between(alquiler.getStartDate(), alquiler.getEndDate()) + 1;
        alquiler.setPrice(Alquiler.PRICE_PER_PERSON_PER_DAY * alquiler.getSeats() * totalDays);

        if (!alquilerRepository.updateAlquiler(alquiler)) {
            acompanantesRepository.deleteAcompanante(id, dniSocio);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        alquiler = alquilerRepository.findAlquilerById(id);
        if (alquiler == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(alquiler, HttpStatus.OK);
    }

    @PatchMapping("/{id}/acompanantes/{dniSocio}")
    public ResponseEntity<Alquiler> removeAcompanante(@PathVariable Integer id, @PathVariable String dniSocio) {
        try {
            return procesarRemoveAcompanante(id, dniSocio);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Alquiler> procesarRemoveAcompanante(Integer id, String dniSocio) {
        Alquiler alquiler = alquilerRepository.findAlquilerById(id);
        if (alquiler == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        if (alquiler.getStartDate().isBefore(LocalDate.now())) return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        if (dniSocio.equals(alquiler.getUserNationalId())) return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        List<Acompanante> acompanantes = acompanantesRepository.findAcompananteByAlquiler(id);
        boolean encontrado = acompanantes.stream().anyMatch(a -> a.getNationalId().equals(dniSocio));
        if (!encontrado) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        if (!acompanantesRepository.deleteAcompanante(id, dniSocio)) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        acompanantes = acompanantesRepository.findAcompananteByAlquiler(id);
        alquiler.setSeats(acompanantes.size() + 1);

        long totalDays = ChronoUnit.DAYS.between(alquiler.getStartDate(), alquiler.getEndDate()) + 1;
        alquiler.setPrice(Alquiler.PRICE_PER_PERSON_PER_DAY * alquiler.getSeats() * totalDays);

        if (!alquilerRepository.updateAlquiler(alquiler)) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        alquiler = alquilerRepository.findAlquilerById(id);
        if (alquiler == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(alquiler, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAlquiler(@PathVariable Integer id) {
        try {
            Alquiler alquiler = alquilerRepository.findAlquilerById(id);
            if (alquiler == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            if (alquiler.getStartDate().isBefore(LocalDate.now())) return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

            boolean exito = alquilerRepository.deleteAlquiler(id);
            return exito ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // --- Métodos de utilidad ---

    /**
     * Refactoring 4.1: Decompose Conditional — se reemplaza la condición compleja
     * de meses por métodos con nombre descriptivo.
     */
    private boolean esDuracionValida(LocalDate startDate, LocalDate endDate) {
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        int mesInicio = startDate.getMonthValue();

        if (esTemporadaBaja(mesInicio)) {
            return totalDays <= MAX_DAYS_LOW_SEASON;
        } else if (esTemporadaAlta(mesInicio)) {
            return totalDays == WEEK || totalDays == TWO_WEEKS;
        }
        return false;
    }

    private boolean esTemporadaBaja(int mes) {
        return mes >= 10 || mes <= 4;
    }

    private boolean esTemporadaAlta(int mes) {
        return mes >= 5 && mes <= 9;
    }

    private long calcularDiasTotales(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    private boolean tieneCamposObligatoriosNulos(Alquiler alquiler) {
        return alquiler.getStartDate() == null || alquiler.getEndDate() == null ||
               alquiler.getUserNationalId() == null || alquiler.getUserNationalId().trim().isEmpty() ||
               alquiler.getBoatRegistration() == null || alquiler.getBoatRegistration().trim().isEmpty();
    }

    private boolean haySuperposicionConAlquiler(LocalDate inicio, LocalDate fin, Alquiler alquilerExistente) {
        return !(fin.isBefore(alquilerExistente.getStartDate()) || inicio.isAfter(alquilerExistente.getEndDate()));
    }

    private boolean haySuperposicionConReserva(LocalDate inicio, LocalDate fin, Reserva reservaExistente) {
        return !(fin.isBefore(reservaExistente.getDate()) || inicio.isAfter(reservaExistente.getDate()));
    }
}