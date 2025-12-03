package com.GM2.api;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Patron;
import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.PatronRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController()
@RequestMapping(path="api/patrones", produces="application/json")
public class PatronRestController {
    private final EmbarcacionRepository embarcacionRepository;
    PatronRepository patronRepository;

    public PatronRestController(PatronRepository patronRepository, EmbarcacionRepository embarcacionRepository) {
        this.patronRepository = patronRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";

        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.embarcacionRepository = embarcacionRepository;
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

    @PatchMapping(path="/{dni}", consumes="application/json")
    public ResponseEntity<Patron> updatePatron(@PathVariable("dni") String dni, @RequestBody Patron newPatron) {

        Patron patronActual = patronRepository.findPatronByDNI(dni);
        if (patronActual == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (newPatron.getNombre() != null) patronActual.setNombre(newPatron.getNombre());
        if (newPatron.getApellidos() != null) patronActual.setApellidos(newPatron.getApellidos());
        if (newPatron.getFechaNacimiento() != null) patronActual.setFechaNacimiento(newPatron.getFechaNacimiento());
        if (newPatron.getFechaExpedicionTitulo() != null) patronActual.setFechaExpedicionTitulo(newPatron.getFechaExpedicionTitulo());

        if (patronActual.getFechaNacimiento().isAfter(LocalDate.now())) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (patronActual.getFechaExpedicionTitulo().isAfter(LocalDate.now())) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (patronActual.getFechaExpedicionTitulo().isBefore(patronActual.getFechaNacimiento())) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        boolean exito = patronRepository.updatePatronInfo(patronActual);

        if (exito) {
            return new ResponseEntity<>(patronActual, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(path="/{matricula}/patron", consumes="application/json")
    public ResponseEntity<Patron> assignPatronToEmbarcacion(@PathVariable String matricula, @RequestBody String dniPatron) {
        //Limpiamos el dni
        String dniPatronLimpio = dniPatron.replaceAll("[\"{}]", "").trim();

        if(embarcacionRepository.findEmbarcacionByMatricula(matricula) == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Patron patron = patronRepository.findPatronByDNI(dniPatronLimpio);
        if(patron == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        //Comprobar que el patrón no está asignado
        if(embarcacionRepository.isPatronAssignedToEmbarcacion(dniPatronLimpio)) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        boolean result = embarcacionRepository.updatePatron(dniPatronLimpio, matricula);

        if(result) {
            return new ResponseEntity<>(patron, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(path="/{matricula}/noPatron", consumes="application/json")
    public ResponseEntity<Patron> unassignPatronToEmbarcacion(@PathVariable String matricula, @RequestBody String dniPatron) {
        //Limpiamos el dni
        String dniPatronLimpio = dniPatron.replaceAll("[\"{}]", "").trim();

        Embarcacion embarcacionActual = embarcacionRepository.findEmbarcacionByMatricula(matricula);
        if( embarcacionActual == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Patron patron = patronRepository.findPatronByDNI(dniPatronLimpio);
        if(patron == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        String patronEmbarcacionActual = embarcacionActual.getIdPatron();

        // Si el barco está vacío O el patrón no coincide, damos error
        if(patronEmbarcacionActual == null || !patronEmbarcacionActual.equals(dniPatronLimpio)) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        boolean result = embarcacionRepository.updatePatron(null, matricula);

        if(result) {
            return new ResponseEntity<>(patron, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
