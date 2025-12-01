package com.GM2.api;

import com.GM2.model.domain.Inscripcion;
import com.GM2.model.repository.HijosRepository;
import com.GM2.model.repository.InscripcionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequestMapping(value = "api/inscripciones", produces = "application/json")
public class InscripcionesRestController {
    InscripcionRepository inscripcionRepository;
    HijosRepository hijosRepository;

    public InscripcionesRestController(InscripcionRepository inscripcionRepository, HijosRepository hijosRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.hijosRepository = hijosRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.inscripcionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.hijosRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /*
    * 1. Obtener la lista de inscripciones individuales (GET)
    * Una inscripción es individual cuando la cuota es igual a 300
    */
    @GetMapping("/individuales")
    public ResponseEntity<List<Inscripcion>> getInscripcionesIndividuales() {
        try {
            List<Inscripcion> todasInscripciones = inscripcionRepository.findAllInscripciones();

            if(todasInscripciones == null || todasInscripciones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            // Filtrar las inscripciones individuales (cuota == 300)
            List<Inscripcion> inscripcionesIndividuales = todasInscripciones.stream()
                .filter(inscripcion -> inscripcion.getCuotaAnual() == 300)
                .collect(Collectors.toList());

            if(inscripcionesIndividuales.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(inscripcionesIndividuales, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /*
    * 2. Obtener la lista de inscripciones familiares (GET)
    * Una inscripción es familiar cuando la cuota es mayor que 300
    */
    @GetMapping("/familiares")
    public ResponseEntity<List<Inscripcion>> getInscripcionesFamiliares() {
        try {
            List<Inscripcion> todasInscripciones = inscripcionRepository.findAllInscripciones();

            if(todasInscripciones == null || todasInscripciones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            // Filtrar las inscripciones familiares (cuota > 300)
            List<Inscripcion> inscripcionesFamiliares = todasInscripciones.stream()
                .filter(inscripcion -> inscripcion.getCuotaAnual() > 300)
                .collect(Collectors.toList());

            if(inscripcionesFamiliares.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(inscripcionesFamiliares, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    /*
    * 3. Obtener la información de una inscripción dado el DNI del socio titular (GET)
    */
    @GetMapping("/titular/{dniTitular}")
    public ResponseEntity<Inscripcion> getInscripcionByDniTitular(@PathVariable String dniTitular) {
        try {
            Inscripcion inscripcion = inscripcionRepository.findInscripcionByDNITitular(dniTitular);

            if(inscripcion == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(inscripcion, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    * 4. Crear una inscripción para un socio titular (POST)
    */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Inscripcion> createInscripcion(@RequestBody Inscripcion inscripcion) {
        try {
            // Validaciones básicas
            if(inscripcion == null || inscripcion.getSocioTitularId() == null || 
               inscripcion.getSocioTitularId().isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            // Intentar crear la inscripción
            boolean resultado = inscripcionRepository.addInscripcion(inscripcion);

            if(resultado) {
                return new ResponseEntity<>(inscripcion, HttpStatus.CREATED);
            } else {
                // Falló la creación (puede que ya exista una inscripción para ese titular)
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}