package com.GM2.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Patron;
import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.PatronRepository;

@RestController
@RequestMapping(path = "api/patrones", produces = "application/json")
public class PatronRestController {
    private final EmbarcacionRepository embarcacionRepository;
    private final PatronRepository patronRepository;

    public PatronRestController(PatronRepository patronRepository, EmbarcacionRepository embarcacionRepository) {
        this.patronRepository = patronRepository;
        this.embarcacionRepository = embarcacionRepository;
    }

    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrones() {
        try {
            List<Patron> patrones = patronRepository.findAllPatrones();
            return (patrones == null || patrones.isEmpty())
                    ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                    : new ResponseEntity<>(patrones, HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Patron> createPatron(@RequestBody Patron nuevoPatron) {
        try { return procesarCreacionPatron(nuevoPatron); }
        catch (Exception e) { return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    private ResponseEntity<Patron> procesarCreacionPatron(Patron p) {
        if (tieneCamposObligatoriosNulos(p)) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        if (patronRepository.isRegistered(p.getNationalId())) return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        if (p.getBirthDate().isAfter(java.time.LocalDate.now())) return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        if (p.getTitleIssueDate().isAfter(java.time.LocalDate.now()) || p.getTitleIssueDate().isBefore(p.getBirthDate()))
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        boolean ok = patronRepository.addPatron(p);
        return ok ? new ResponseEntity<>(p, HttpStatus.CREATED) : new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean tieneCamposObligatoriosNulos(Patron p) {
        return p.getNationalId() == null || p.getNationalId().trim().isEmpty() ||
               p.getName() == null || p.getName().trim().isEmpty() ||
               p.getSurname() == null || p.getSurname().trim().isEmpty() ||
               p.getBirthDate() == null || p.getTitleIssueDate() == null;
    }

    @PatchMapping(path = "/{dni}", consumes = "application/json")
    public ResponseEntity<Patron> updatePatron(@PathVariable("dni") String dni, @RequestBody Patron newPatron) {
        Patron actual = patronRepository.findPatronByDNI(dni);
        if (actual == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        if (newPatron.getNationalId() != null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        if (newPatron.getName() != null) actual.setName(newPatron.getName());
        if (newPatron.getSurname() != null) actual.setSurname(newPatron.getSurname());
        if (newPatron.getBirthDate() != null) actual.setBirthDate(newPatron.getBirthDate());
        if (newPatron.getTitleIssueDate() != null) actual.setTitleIssueDate(newPatron.getTitleIssueDate());
        if (actual.getBirthDate().isAfter(java.time.LocalDate.now())) return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        if (actual.getTitleIssueDate().isAfter(java.time.LocalDate.now())) return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        if (actual.getTitleIssueDate().isBefore(actual.getBirthDate())) return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        boolean ok = patronRepository.updatePatron(actual);
        return ok ? new ResponseEntity<>(actual, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PatchMapping(path = "/{matricula}/patron")
    public ResponseEntity<Patron> assignPatronToEmbarcacion(@PathVariable String matricula, @RequestBody String dniPatron) {
        String dniLimpio = dniPatron.replaceAll("[\"{}]", "").trim();
        if (embarcacionRepository.findEmbarcacionByMatricula(matricula) == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        Patron patron = patronRepository.findPatronByDNI(dniLimpio);
        if (patron == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        if (embarcacionRepository.isPatronAssignedToEmbarcacion(dniLimpio)) return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        boolean ok = embarcacionRepository.updatePatron(dniLimpio, matricula);
        return ok ? new ResponseEntity<>(patron, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PatchMapping(path = "/{matricula}/noPatron")
    public ResponseEntity<Patron> unassignPatronToEmbarcacion(@PathVariable String matricula, @RequestBody String dniPatron) {
        String dniLimpio = dniPatron.replaceAll("[\"{}]", "").trim();
        Embarcacion emb = embarcacionRepository.findEmbarcacionByMatricula(matricula);
        if (emb == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        Patron patron = patronRepository.findPatronByDNI(dniLimpio);
        if (patron == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        String patronActual = emb.getSkipperId();
        if (patronActual == null || !patronActual.equals(dniLimpio)) return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        boolean ok = embarcacionRepository.updatePatron(null, matricula);
        return ok ? new ResponseEntity<>(patron, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> deletePatron(@PathVariable String dni) {
        if (patronRepository.findPatronByDNI(dni) == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (embarcacionRepository.isPatronAssignedToEmbarcacion(dni)) return new ResponseEntity<>(HttpStatus.CONFLICT);
        boolean ok = patronRepository.deletePatron(dni);
        return ok ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<Patron> getPatronByDni(@PathVariable String dni) {
        Patron patron = patronRepository.findPatronByDNI(dni);
        return patron != null ? new ResponseEntity<>(patron, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}