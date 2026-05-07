package com.GM2.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

import com.GM2.model.domain.Hijos;
import com.GM2.model.domain.Inscripcion;
import com.GM2.model.domain.Socio;
import com.GM2.model.domain.SocioConInscripcionRequest;
import com.GM2.model.repository.HijosRepository;
import com.GM2.model.repository.InscripcionRepository;
import com.GM2.model.repository.SocioRepository;

/**
 * Controlador REST de la API de socios.
 * Permite realizar operaciones CRUD sobre socios.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
@RestController
@RequestMapping(value = "api/socios", produces = "application/json")
public class SociosRestController {
    SocioRepository socioRepository;
    InscripcionRepository inscripcionRepository;
    HijosRepository hijosRepository;

    /**
     * Constructor de la clase.
     * 
     * @param socioRepository Instancia de SocioRepository
     * @param inscripcionRepository Instancia de InscripcionRepository
     * @param hijosRepository Instancia de HijosRepository
     */
    public SociosRestController(SocioRepository socioRepository, InscripcionRepository inscripcionRepository, HijosRepository hijosRepository) {
        this.socioRepository = socioRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.hijosRepository = hijosRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.inscripcionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.hijosRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * 1. Obtener la lista completa de socios (GET)
     * 
     * @return ResponseEntity con la lista de socios y estado 200 (OK) si se ha podido obtener correctamente
     * y en caso de error: 204 (No Content) o 500 (Internal Server Error)
     */
    @GetMapping
    public ResponseEntity<List<Socio>> getAllSocios() {
        try {
            List<Socio> socios = socioRepository.findAllSocios();

            if (socios == null || socios.isEmpty() ) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            //Clean Code - Reglas de comentarios: Comentario TO-DO

            return new ResponseEntity<>(socios, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 2. Obtener la información de un socio dado su DNI (GET)
     * 
     * @param dni DNI del socio
     * @return ResponseEntity con la información del socio y estado 200 (OK) si se ha podido obtener correctamente
     * y en caso de error: 204 (No Content) o 500 (Internal Server Error)
     */
    @GetMapping("/{dni}")
    public ResponseEntity<Socio> getSocioByDNI(@PathVariable String dni) {
        try {
            Socio socio = socioRepository.findSocioByDNI(dni);

            if (socio == null ) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(socio, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 3. Crear socio sin inscripción (POST)
     * 
     * @param socio Objeto Socio a crear
     * @return ResponseEntity con el socio creado y estado 200 (OK) si se ha podido crear correctamente
     * y en caso de error: 400 (Bad Request) o 500 (Internal Server Error)
     */
    @PostMapping(value = "/socioSinInscripcion", consumes = "application/json")
    public ResponseEntity<Socio> createSocioSinInscripcion(@RequestBody Socio socio) {
        // Validaciones
        if (socio.getNationalId().isEmpty() || socio.getName().isEmpty() || socio.getSurname().isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (socio.getBirthDate().getYear() > 2007) {
            Hijos hijo = new Hijos(socio.getNationalId(), socio.getName(), socio.getSurname(), socio.getBirthDate());
            hijo.setRegistrationId(0); // Sin inscripción

            try {
                Boolean res = hijosRepository.addHijo(hijo) == true;

                if (res) {
                    return new ResponseEntity<>(socio, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            socio.setIsTitular(false);
            socio.setRegistrationDate(LocalDate.now());

            try {
                if (socio.getIsTitular() != null && socio.getIsTitular()) {
                    socioRepository.addSocioTitular(socio);
                } else {
                    socioRepository.addSocioNoTitular(socio);
                }
                return new ResponseEntity<>(socio, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * 4. Crear socio asociandolo a una inscripción familiar ya existente (POST)
     * 
     * @param socioConInscripcion Objeto que contiene los datos del socio y el DNI del titular
     * @return ResponseEntity con el socio creado y estado 201 (Created) si se ha podido crear correctamente
     * y en caso de error: 400 (Bad Request), 404 (Not Found) o 500 (Internal Server Error)
     */
    @PostMapping(value = "/socioConInscripcion", consumes = "application/json")
    public ResponseEntity<Socio> createSocioConInscripcion(@RequestBody SocioConInscripcionRequest socioConInscripcion) {
        if (socioConInscripcion.getNationalId() == null || socioConInscripcion.getNationalId().isEmpty() ||
           socioConInscripcion.getName() == null || socioConInscripcion.getName().isEmpty() ||
           socioConInscripcion.getSurname() == null || socioConInscripcion.getSurname().isEmpty() ||
           socioConInscripcion.getAddress() == null || socioConInscripcion.getAddress().isEmpty() ||
           socioConInscripcion.getBirthDate() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (socioConInscripcion.getTitularNationalId() == null || socioConInscripcion.getTitularNationalId().isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        try {
            return procesarCreacionSocioConInscripcion(socioConInscripcion);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Socio> procesarCreacionSocioConInscripcion(SocioConInscripcionRequest socioConInscripcion) {
        Inscripcion inscripcion = inscripcionRepository.findInscripcionByDNITitular(socioConInscripcion.getTitularNationalId());

        if (inscripcion == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Socio socio = new Socio(socioConInscripcion.getName(), socioConInscripcion.getSurname(), socioConInscripcion.getNationalId(), socioConInscripcion.getBirthDate(), socioConInscripcion.getAddress(), LocalDate.now(), socioConInscripcion.getIsSkipper());

        if (socio.getBirthDate().getYear() > 2007) {
            Hijos hijo = new Hijos(socio.getNationalId(), socio.getName(), socio.getSurname(), socio.getBirthDate());
            hijo.setRegistrationId(inscripcion.getId());

            Boolean res = hijosRepository.addHijo(hijo);
            inscripcion.setAnnualFee(inscripcion.getAnnualFee() + 100);

            if (!res) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            if (socio.getIsTitular() != null && socio.getIsTitular()) {
                socioRepository.addSocioTitular(socio);
            } else {
                socioRepository.addSocioNoTitular(socio);
            }

            if (inscripcion.getSecondAdult() == null) {
                inscripcion.setAnnualFee(inscripcion.getAnnualFee() + 250);
            }

            inscripcion.setSecondAdult(socio.getNationalId());
        }

        inscripcionRepository.updateInscripcion(inscripcion);

        return new ResponseEntity<>(socio, HttpStatus.CREATED);
    }

    /**
     * 5. Actualizar los campos de información de un socio, excepto el DNI (PATCH)
     * 
     * @param dni DNI del socio a actualizar
     * @param updates Mapa con los campos a actualizar
     * @return ResponseEntity con el socio actualizado y estado 200 (OK) si se ha podido actualizar correctamente
     * y en caso de error: 404 (Not Found) o 500 (Internal Server Error)
     */
    @PatchMapping(value = "/{dni}", consumes = "application/json")
    public ResponseEntity<Socio> updateSocio(@PathVariable String dni, @RequestBody Map<String, Object> updates) {
        try {
            return procesarUpdateSocio(dni, updates);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Socio> procesarUpdateSocio(String dni, Map<String, Object> updates) {
        Socio socio = socioRepository.findSocioByDNI(dni);

        if (socio == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (updates.containsKey("nombre")) {
            socio.setName((String) updates.get("nombre"));
        }
        if (updates.containsKey("apellidos")) {
            socio.setSurname((String) updates.get("apellidos"));
        }
        if (updates.containsKey("fechaNacimiento")) {
            socio.setBirthDate(LocalDate.parse((String) updates.get("fechaNacimiento")));
        }
        if (updates.containsKey("direccion")) {
            socio.setAddress((String) updates.get("direccion"));
        }
        if (updates.containsKey("fechaInscripcion")) {
            socio.setRegistrationDate(LocalDate.parse((String) updates.get("fechaInscripcion")));
        }
        if (updates.containsKey("esTitular")) {
            socio.setIsTitular((Boolean) updates.get("esTitular"));
        }
        if (updates.containsKey("tieneLicenciaPatron")) {
            socio.setHasSkipperLicense((Boolean) updates.get("tieneLicenciaPatron"));
        }

        socioRepository.updateSocio(socio);
        return new ResponseEntity<>(socio, HttpStatus.OK);
    }

    /**
     * 6. Eliminar a un socio si no está vinculado a ninguna inscripción (DELETE)
     * Busca en la tabla de socios y en la tabla de hijos
     * 
     * @param dni DNI del socio a eliminar
     * @return ResponseEntity con estado 204 (No Content) si se ha podido eliminar correctamente
     * y en caso de error: 400 (Bad Request), 404 (Not Found), 409 (Conflict) o 500 (Internal Server Error)
     */
    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> deleteSocio(@PathVariable String dni) {
        try {
            return procesarDeleteSocio(dni);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Void> procesarDeleteSocio(String dni) {
        if (dni == null || dni.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Socio socio = socioRepository.findSocioByDNI(dni);

        if (socio != null) {
            if (socio.getIsTitular()) {
                Inscripcion inscripcion = inscripcionRepository.findInscripcionByDNITitular(dni);
                if (inscripcion != null) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            }

            List<Inscripcion> todasInscripciones = inscripcionRepository.findAllInscripciones();
            if (todasInscripciones != null) {
                for (Inscripcion insc : todasInscripciones) {
                    if (insc.getSecondAdult() != null && insc.getSecondAdult().equals(dni)) {
                        return new ResponseEntity<>(HttpStatus.CONFLICT);
                    }
                }
            }

            boolean resultado = socioRepository.deleteSocio(dni);
            if (resultado) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        Hijos hijo = hijosRepository.findHijoByDni(dni);

        if (hijo != null) {
            if (hijo.getRegistrationId() > 0) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            boolean resultado = hijosRepository.deleteHijo(dni);
            if (resultado) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
