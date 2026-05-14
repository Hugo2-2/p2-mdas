package com.GM2.api;

import java.util.Arrays;
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
import org.springframework.web.bind.annotation.RestController;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.repository.EmbarcacionRepository;

/**
 * Controlador REST para la gestión de recursos de tipo Embarcacion.
 *
 * Refactoring 1.2: Extract Method — se han extraído las validaciones de campos
 * individuales en el PATCH a métodos descriptivos.
 * Refactoring 2.3: Eliminada configuración de SQL path en constructor.
 *
 * @author gm2equipo1
 * @version 1.0
 */
@RestController
@RequestMapping(path = "api/embarcaciones", produces = "application/json")
public class EmbarcacionRestController {

    private static final List<String> TIPOS_VALIDOS = Arrays.asList("VELERO", "YATE", "LANCHA", "BARCA_PESCA", "CATAMARAN");
    private static final int MIN_SEATS = 2;
    private static final double MIN_DIMENSIONS = 1.0;

    private final EmbarcacionRepository embarcacionRepository;

    public EmbarcacionRestController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
    }

    @GetMapping
    public ResponseEntity<List<Embarcacion>> getAllEmbarcaciones() {
        try {
            List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcaciones();
            if (embarcaciones == null || embarcaciones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(embarcaciones, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(params = "tipo")
    public ResponseEntity<List<Embarcacion>> getEmbarcacionesByTipo(String tipo) {
        try {
            List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcacionesByTipo(tipo);
            if (embarcaciones == null || embarcaciones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(embarcaciones, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Embarcacion> createEmbarcacion(@RequestBody Embarcacion nuevaEmbarcacion) {
        try {
            return procesarCreacionEmbarcacion(nuevaEmbarcacion);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Embarcacion> procesarCreacionEmbarcacion(Embarcacion nuevaEmbarcacion) {
        if (tieneCamposObligatoriosVacios(nuevaEmbarcacion)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        nuevaEmbarcacion.setSkipperId(null);

        if (embarcacionRepository.findEmbarcacionByMatricula(nuevaEmbarcacion.getRegistration()) != null) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (embarcacionRepository.findEmbarcacionByNombre(nuevaEmbarcacion.getName()) != null) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        boolean exito = embarcacionRepository.addEmbarcacion(nuevaEmbarcacion);
        return exito
                ? new ResponseEntity<>(nuevaEmbarcacion, HttpStatus.CREATED)
                : new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Refactoring 1.1: Extract Method — validación de campos obligatorios.
     */
    private boolean tieneCamposObligatoriosVacios(Embarcacion e) {
        return e.getRegistration() == null || e.getRegistration().trim().isEmpty() ||
               e.getName() == null || e.getName().trim().isEmpty() ||
               e.getType() == null || e.getType().trim().isEmpty() ||
               e.getDimensions() == null || e.getDimensions().trim().isEmpty() ||
               e.getSeats() <= 0;
    }

    @PatchMapping(path = "/{matricula}", consumes = "application/json")
    public ResponseEntity<Embarcacion> updateEmbarcacion(@PathVariable String matricula, @RequestBody Embarcacion nuevaEmbarcacion) {
        try {
            return procesarUpdateEmbarcacion(matricula, nuevaEmbarcacion);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Refactoring 1.2: Extract Method — las validaciones de cada campo individual
     * (nombre, tipo, plazas, dimensiones) se mantienen como bloques claros con guard clauses.
     */
    private ResponseEntity<Embarcacion> procesarUpdateEmbarcacion(String matricula, Embarcacion nuevaEmbarcacion) {
        Embarcacion embarcacionActual = embarcacionRepository.findEmbarcacionByMatricula(matricula);
        if (embarcacionActual == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        if (nuevaEmbarcacion.getRegistration() != null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        // Aplicar cambio de nombre (con validación de unicidad)
        if (nuevaEmbarcacion.getName() != null && !nuevaEmbarcacion.getName().equals(embarcacionActual.getName())) {
            if (embarcacionRepository.findEmbarcacionByNombre(nuevaEmbarcacion.getName()) != null) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }
            embarcacionActual.setName(nuevaEmbarcacion.getName());
        }

        // Aplicar cambio de tipo (con validación de valor permitido)
        if (nuevaEmbarcacion.getType() != null) {
            if (!TIPOS_VALIDOS.contains(nuevaEmbarcacion.getType())) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            embarcacionActual.setType(nuevaEmbarcacion.getType());
        }

        // Aplicar cambio de plazas (con validación de mínimo)
        if (nuevaEmbarcacion.getSeats() != 0) {
            if (nuevaEmbarcacion.getSeats() < MIN_SEATS) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            embarcacionActual.setSeats(nuevaEmbarcacion.getSeats());
        }

        // Aplicar cambio de dimensiones (con validación de formato numérico y mínimo)
        if (nuevaEmbarcacion.getDimensions() != null) {
            try {
                double dimensionesNum = Double.parseDouble(nuevaEmbarcacion.getDimensions());
                if (dimensionesNum < MIN_DIMENSIONS) {
                    return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                embarcacionActual.setDimensions(nuevaEmbarcacion.getDimensions());
            } catch (NumberFormatException e) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }

        boolean exito = embarcacionRepository.updateEmbarcacion(embarcacionActual);
        return exito
                ? new ResponseEntity<>(embarcacionActual, HttpStatus.OK)
                : new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> deleteEmbarcacion(@PathVariable String matricula) {
        if (embarcacionRepository.findEmbarcacionByMatricula(matricula) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (embarcacionRepository.isEmbarcacionAlquilada(matricula)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (embarcacionRepository.isEmbarcacionReservada(matricula)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        boolean exito = embarcacionRepository.deleteEmbarcacion(matricula);
        return exito ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<Embarcacion> getEmbarcacionByMatricula(@PathVariable String matricula) {
        Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(matricula);
        return embarcacion != null
                ? new ResponseEntity<>(embarcacion, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}