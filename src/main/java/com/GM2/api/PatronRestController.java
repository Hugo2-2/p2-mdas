package com.GM2.api;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Patron;
import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.PatronRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(path="api/patrones", produces="application/json")
public class PatronRestController {
    PatronRepository patronRepository;

    public PatronRestController(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";

        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrones() {
        try {
            List<Patron> patrones = patronRepository.findAllPatrones();

            if (patrones == null || patrones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(patrones, HttpStatus.OK);

        } catch (Exception e) {
            // Si falla la conexión a la BD o hay otro error inesperado
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Patron> createPatron(@RequestBody Patron nuevoPatron) {

        if (patronRepository.isRegistered(nuevoPatron.getDni())) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        if (nuevoPatron.getFechaNacimiento().isAfter(java.time.LocalDate.now())) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (nuevoPatron.getFechaExpedicionTitulo().isAfter(java.time.LocalDate.now()) ||
                nuevoPatron.getFechaExpedicionTitulo().isBefore(nuevoPatron.getFechaNacimiento())) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        boolean exito = patronRepository.addPatron(nuevoPatron);

        if (exito) {
            return new ResponseEntity<>(nuevoPatron, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
