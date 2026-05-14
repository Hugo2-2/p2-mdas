package com.GM2.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.GM2.model.domain.Hijos;
import com.GM2.model.domain.Inscripcion;
import com.GM2.model.domain.Socio;
import com.GM2.model.domain.SocioConInscripcionRequest;
import com.GM2.model.repository.HijosRepository;
import com.GM2.model.repository.InscripcionRepository;
import com.GM2.model.repository.SocioRepository;

@RestController
@RequestMapping(value = "api/socios", produces = "application/json")
public class SociosRestController {
    private final SocioRepository socioRepository;
    private final InscripcionRepository inscripcionRepository;
    private final HijosRepository hijosRepository;

    public SociosRestController(SocioRepository socioRepository, InscripcionRepository inscripcionRepository, HijosRepository hijosRepository) {
        this.socioRepository = socioRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.hijosRepository = hijosRepository;
    }

    @GetMapping
    public ResponseEntity<List<Socio>> getAllSocios() {
        try {
            List<Socio> socios = socioRepository.findAllSocios();
            return (socios == null || socios.isEmpty()) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(socios, HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @GetMapping("/{dni}")
    public ResponseEntity<Socio> getSocioByDNI(@PathVariable String dni) {
        try {
            Socio socio = socioRepository.findSocioByDNI(dni);
            return socio == null ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(socio, HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PostMapping(value = "/socioSinInscripcion", consumes = "application/json")
    public ResponseEntity<Socio> createSocioSinInscripcion(@RequestBody Socio socio) {
        if (socio.getNationalId().isEmpty() || socio.getName().isEmpty() || socio.getSurname().isEmpty())
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        boolean esMenor = !socio.isOfLegalAge(LocalDate.now());
        if (esMenor) {
            return registrarComoHijo(socio);
        } else {
            return registrarComoSocioAdulto(socio);
        }
    }

    /**
     * Refactoring 1.3: Extract Method — separada la lógica de registro de menores.
     */
    private ResponseEntity<Socio> registrarComoHijo(Socio socio) {
        Hijos hijo = new Hijos(socio.getNationalId(), socio.getName(), socio.getSurname(), socio.getBirthDate());
        hijo.setRegistrationId(0);
        try {
            boolean ok = hijosRepository.addHijo(hijo);
            return ok ? new ResponseEntity<>(socio, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) { return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    /**
     * Refactoring 1.3: Extract Method — separada la lógica de registro de adultos.
     */
    private ResponseEntity<Socio> registrarComoSocioAdulto(Socio socio) {
        socio.setIsTitular(false);
        socio.setRegistrationDate(LocalDate.now());
        try {
            socioRepository.addSocioNoTitular(socio);
            return new ResponseEntity<>(socio, HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PostMapping(value = "/socioConInscripcion", consumes = "application/json")
    public ResponseEntity<Socio> createSocioConInscripcion(@RequestBody SocioConInscripcionRequest req) {
        if (req.getNationalId() == null || req.getNationalId().isEmpty() ||
            req.getName() == null || req.getName().isEmpty() ||
            req.getSurname() == null || req.getSurname().isEmpty() ||
            req.getAddress() == null || req.getAddress().isEmpty() ||
            req.getBirthDate() == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        if (req.getTitularNationalId() == null || req.getTitularNationalId().isEmpty()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        try { return procesarCreacionSocioConInscripcion(req); }
        catch (Exception e) { e.printStackTrace(); return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    private ResponseEntity<Socio> procesarCreacionSocioConInscripcion(SocioConInscripcionRequest req) {
        Inscripcion inscripcion = inscripcionRepository.findInscripcionByDNITitular(req.getTitularNationalId());
        if (inscripcion == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        Socio socio = new Socio(req.getName(), req.getSurname(), req.getNationalId(), req.getBirthDate(), req.getAddress(), LocalDate.now(), req.getIsSkipper());

        boolean esMenor = !socio.isOfLegalAge(LocalDate.now());
        if (esMenor) {
            Hijos hijo = new Hijos(socio.getNationalId(), socio.getName(), socio.getSurname(), socio.getBirthDate());
            hijo.setRegistrationId(inscripcion.getId());
            boolean ok = hijosRepository.addHijo(hijo);
            inscripcion.setAnnualFee(inscripcion.getAnnualFee() + Inscripcion.CHILD_FEE);
            if (!ok) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            if (socio.getIsTitular() != null && socio.getIsTitular()) { socioRepository.addSocioTitular(socio); }
            else { socioRepository.addSocioNoTitular(socio); }
            if (inscripcion.getSecondAdult() == null) { inscripcion.setAnnualFee(inscripcion.getAnnualFee() + Inscripcion.SECOND_ADULT_FEE); }
            inscripcion.setSecondAdult(socio.getNationalId());
        }
        inscripcionRepository.updateInscripcion(inscripcion);
        return new ResponseEntity<>(socio, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{dni}", consumes = "application/json")
    public ResponseEntity<Socio> updateSocio(@PathVariable String dni, @RequestBody Map<String, Object> updates) {
        try {
            Socio socio = socioRepository.findSocioByDNI(dni);
            if (socio == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            if (updates.containsKey("nombre")) socio.setName((String) updates.get("nombre"));
            if (updates.containsKey("apellidos")) socio.setSurname((String) updates.get("apellidos"));
            if (updates.containsKey("fechaNacimiento")) socio.setBirthDate(LocalDate.parse((String) updates.get("fechaNacimiento")));
            if (updates.containsKey("direccion")) socio.setAddress((String) updates.get("direccion"));
            if (updates.containsKey("fechaInscripcion")) socio.setRegistrationDate(LocalDate.parse((String) updates.get("fechaInscripcion")));
            if (updates.containsKey("esTitular")) socio.setIsTitular((Boolean) updates.get("esTitular"));
            if (updates.containsKey("tieneLicenciaPatron")) socio.setHasSkipperLicense((Boolean) updates.get("tieneLicenciaPatron"));
            socioRepository.updateSocio(socio);
            return new ResponseEntity<>(socio, HttpStatus.OK);
        } catch (Exception e) { e.printStackTrace(); return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> deleteSocio(@PathVariable String dni) {
        try { return procesarDeleteSocio(dni); }
        catch (Exception e) { e.printStackTrace(); return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    /**
     * Refactoring 1.3: Extract Method — separadas las verificaciones de vinculación.
     */
    private ResponseEntity<Void> procesarDeleteSocio(String dni) {
        if (dni == null || dni.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Socio socio = socioRepository.findSocioByDNI(dni);
        if (socio != null) {
            if (estaVinculadoAInscripcion(socio, dni)) return new ResponseEntity<>(HttpStatus.CONFLICT);
            boolean ok = socioRepository.deleteSocio(dni);
            return ok ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Hijos hijo = hijosRepository.findHijoByDni(dni);
        if (hijo != null) {
            if (hijo.getRegistrationId() > 0) return new ResponseEntity<>(HttpStatus.CONFLICT);
            boolean ok = hijosRepository.deleteHijo(dni);
            return ok ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private boolean estaVinculadoAInscripcion(Socio socio, String dni) {
        if (socio.getIsTitular()) {
            Inscripcion insc = inscripcionRepository.findInscripcionByDNITitular(dni);
            if (insc != null) return true;
        }
        List<Inscripcion> todas = inscripcionRepository.findAllInscripciones();
        if (todas != null) {
            for (Inscripcion insc : todas) {
                if (insc.getSecondAdult() != null && insc.getSecondAdult().equals(dni)) return true;
            }
        }
        return false;
    }
}
