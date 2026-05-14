package com.GM2.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.GM2.model.domain.Hijos;
import com.GM2.model.domain.Inscripcion;
import com.GM2.model.domain.Socio;
import com.GM2.model.repository.HijosRepository;
import com.GM2.model.repository.InscripcionRepository;
import com.GM2.model.repository.SocioRepository;

@RestController
@RequestMapping(value = "api/inscripciones", produces = "application/json")
public class InscripcionesRestController {
    private final InscripcionRepository inscripcionRepository;
    private final HijosRepository hijosRepository;
    private final SocioRepository socioRepository;

    public InscripcionesRestController(InscripcionRepository inscripcionRepository, HijosRepository hijosRepository, SocioRepository socioRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.hijosRepository = hijosRepository;
        this.socioRepository = socioRepository;
    }

    @GetMapping("/individuales")
    public ResponseEntity<List<Inscripcion>> getInscripcionesIndividuales() {
        try {
            List<Inscripcion> todas = inscripcionRepository.findAllInscripciones();
            if (todas == null || todas.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            List<Inscripcion> individuales = todas.stream().filter(i -> i.getAnnualFee() == Inscripcion.BASE_ANNUAL_FEE).collect(Collectors.toList());
            return individuales.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(individuales, HttpStatus.OK);
        } catch (Exception e) { e.printStackTrace(); return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @GetMapping("/familiares")
    public ResponseEntity<List<Inscripcion>> getInscripcionesFamiliares() {
        try {
            List<Inscripcion> todas = inscripcionRepository.findAllInscripciones();
            if (todas == null || todas.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            List<Inscripcion> familiares = todas.stream().filter(i -> i.getAnnualFee() > Inscripcion.BASE_ANNUAL_FEE).collect(Collectors.toList());
            return familiares.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(familiares, HttpStatus.OK);
        } catch (Exception e) { e.printStackTrace(); return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @GetMapping("/titular/{dniTitular}")
    public ResponseEntity<Inscripcion> getInscripcionByDniTitular(@PathVariable String dniTitular) {
        try {
            Inscripcion inscripcion = inscripcionRepository.findInscripcionByDNITitular(dniTitular);
            return inscripcion == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(inscripcion, HttpStatus.OK);
        } catch (Exception e) { e.printStackTrace(); return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Inscripcion> createInscripcion(@RequestBody Inscripcion body) {
        try {
            Inscripcion inscripcion = new Inscripcion(body.getTitularMemberId(), body.getSecondAdult(), body.getChildren());
            if (inscripcion.getTitularMemberId() == null || inscripcion.getTitularMemberId().isEmpty()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            if (socioRepository.findSocioByDNI(inscripcion.getTitularMemberId()) == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            if (inscripcionRepository.findInscripcionByDNITitular(inscripcion.getTitularMemberId()) != null) return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            boolean ok = inscripcionRepository.addInscripcion(inscripcion);
            if (ok) {
                Socio titular = socioRepository.findSocioByDNI(inscripcion.getTitularMemberId());
                titular.setIsTitular(true);
                socioRepository.updateSocio(titular);
                return new ResponseEntity<>(inscripcionRepository.findInscripcionByDNITitular(inscripcion.getTitularMemberId()), HttpStatus.CREATED);
            }
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } catch (Exception e) { e.printStackTrace(); return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PatchMapping(value = "/addMiembro/{idInscripcion}")
    public ResponseEntity<Inscripcion> addMiembroAFamiliar(@PathVariable int idInscripcion, @RequestBody String dniNuevoMiembro) {
        try {
            if (dniNuevoMiembro == null || dniNuevoMiembro.isEmpty()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            Inscripcion inscripcion = inscripcionRepository.findInscripcionById(idInscripcion);
            if (inscripcion == null || inscripcion.getTitularMemberId() == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            boolean resHijos = false;
            Socio socio = socioRepository.findSocioByDNI(dniNuevoMiembro);
            boolean esSocioNoTitular = socio != null && !socio.getIsTitular();
            if (esSocioNoTitular) { inscripcion.setSecondAdult(dniNuevoMiembro); inscripcion.setAnnualFee(inscripcion.getAnnualFee() + Inscripcion.SECOND_ADULT_FEE); }
            Hijos hijo = hijosRepository.findHijoByDni(dniNuevoMiembro);
            if (hijo != null) { hijo.setRegistrationId(idInscripcion); resHijos = hijosRepository.updateHijo(hijo); inscripcion.setAnnualFee(inscripcion.getAnnualFee() + Inscripcion.CHILD_FEE); }
            inscripcionRepository.updateInscripcion(inscripcion);
            return (hijo == null || resHijos) ? new ResponseEntity<>(inscripcionRepository.findInscripcionById(idInscripcion), HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) { e.printStackTrace(); return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PatchMapping(value = "/removeMiembro/{idInscripcion}")
    public ResponseEntity<Inscripcion> removeMiembroDeFamiliar(@PathVariable int idInscripcion, @RequestBody String dniMiembro) {
        try {
            if (dniMiembro == null || dniMiembro.isEmpty()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            Inscripcion inscripcion = inscripcionRepository.findInscripcionById(idInscripcion);
            if (inscripcion == null || inscripcion.getTitularMemberId() == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            boolean resHijo = false;
            Socio socio = socioRepository.findSocioByDNI(dniMiembro);
            if (socio != null) { inscripcion.setSecondAdult(null); inscripcion.setAnnualFee(inscripcion.getAnnualFee() - Inscripcion.SECOND_ADULT_FEE); }
            Hijos hijo = hijosRepository.findHijoByDni(dniMiembro);
            if (hijo != null) { hijo.setRegistrationId(0); inscripcion.setAnnualFee(inscripcion.getAnnualFee() - Inscripcion.CHILD_FEE); resHijo = hijosRepository.updateHijo(hijo); }
            inscripcionRepository.updateInscripcion(inscripcion);
            inscripcion = inscripcionRepository.findInscripcionById(idInscripcion);
            return (hijo == null || resHijo) ? new ResponseEntity<>(inscripcion, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) { e.printStackTrace(); return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> deleteInscripcion(@PathVariable("dni") String dniTitular) {
        try {
            if (dniTitular == null || dniTitular.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            Inscripcion inscripcion = inscripcionRepository.findInscripcionByDNITitular(dniTitular);
            if (inscripcion == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            boolean ok = inscripcionRepository.deleteInscripcionByDniTitular(dniTitular);
            return ok ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) { e.printStackTrace(); return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); }
    }
}