package com.GM2.api;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.repository.EmbarcacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(path="api/embarcaciones", produces="application/json")
public class EmbarcacionRestController {
    EmbarcacionRepository embarcacionRepository;

    public EmbarcacionRestController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";

        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping
    public ResponseEntity<List<Embarcacion>> getAllEmbarcaciones() {
        try {
            List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcaciones();

            if (embarcaciones == null || embarcaciones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(embarcaciones, HttpStatus.OK);

        } catch (Exception e) {
            // Si falla la conexión a la BD o hay otro error inesperado
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
            // Si falla la conexión a la BD o hay otro error inesperado
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Embarcacion> createEmbarcacion(@RequestBody Embarcacion nuevaEmbarcacion) {
        try {
            //No se le debe asociar ningún patron, para cumplir lo que dice el enunciado
            nuevaEmbarcacion.setIdPatron(null);

            if (embarcacionRepository.findEmbarcacionByMatricula(nuevaEmbarcacion.getMatricula()) != null) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            if (embarcacionRepository.findEmbarcacionByNombre(nuevaEmbarcacion.getNombre()) != null) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            boolean exito = embarcacionRepository.addEmbarcacion(nuevaEmbarcacion);

            if (exito) {
                return new ResponseEntity<>(nuevaEmbarcacion, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
