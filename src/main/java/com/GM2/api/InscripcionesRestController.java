package com.GM2.api;

import com.GM2.model.domain.Hijos;
import com.GM2.model.domain.Inscripcion;
import com.GM2.model.domain.Socio;
import com.GM2.model.repository.HijosRepository;
import com.GM2.model.repository.InscripcionRepository;
import com.GM2.model.repository.SocioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequestMapping(value = "api/inscripciones", produces = "application/json")
public class InscripcionesRestController {
    InscripcionRepository inscripcionRepository;
    HijosRepository hijosRepository;
    SocioRepository socioRepository;

    public InscripcionesRestController(InscripcionRepository inscripcionRepository, HijosRepository hijosRepository, SocioRepository socioRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.hijosRepository = hijosRepository;
        this.socioRepository = socioRepository;


        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.inscripcionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.hijosRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
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

    /*

    * 5. Actualizar una inscripción individual para convertirla en una familiar (PUT)

    Esta API no será necesaria en nuestro proyecto, ya que de la forma que tenemos diseñado el sistema
    de inscripciones, una inscripción individual nunca se convierte en familiar. Todas las inscripciones
    son iguales, la diferencia está en que las individuales tienen solo un socio (el titular) y las familiares
    tienen varios socios (el titular y miembros familiares). Por lo tanto, no es necesario actualizar.

    */

    /*
    * 6. Vincular a un nuevo miembro en una inscripción familiar, asumiendo que el nuevo
    miembro ya se ha registrado previamente como socio (PATCH)

    */
    @PatchMapping(value = "/addMiembro/{idInscripcion}", consumes = "application/json")
    public ResponseEntity<Inscripcion> addMiembroAFamiliar( @PathVariable int idInscripcion, @RequestBody String dniNuevoMiembro ) {

        try {
            // Validaciones básicas
            if(dniNuevoMiembro == null || dniNuevoMiembro.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            Inscripcion inscripcion = inscripcionRepository.findInscripcionById(idInscripcion);

            if(inscripcion == null || inscripcion.getSocioTitularId() == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            boolean resultado = false;

            // Validar si estamos añadiendo un hijo o un segundo adulto
            Socio socio = socioRepository.findSocioByDNI(dniNuevoMiembro);

            if(socio != null) {
                inscripcion.setSegundoAudlto(dniNuevoMiembro);
                resultado = inscripcionRepository.updateInscripcion(inscripcion).equals("EXITO");
            }

            Hijos hijo = hijosRepository.findHijoByDni(dniNuevoMiembro);
            if(hijo != null) {
                hijo.setId_inscripcion(idInscripcion);
                resultado = hijosRepository.updateHijo(hijo) == true;
            }

            if(resultado == true) {
                return new ResponseEntity<>(inscripcion, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /*
    * 7. Desvincular a un miembro de una inscripción familiar, sin borrar al socio (PATCH)
    */
    @PatchMapping(value = "/removeMiembro/{idInscripcion}", consumes = "application/json")
    public ResponseEntity<Inscripcion> removeMiembroDeFamiliar( @PathVariable int idInscripcion, @RequestBody String dniMiembro ) {

        try {
            if( dniMiembro == null || dniMiembro.isEmpty() ) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            Inscripcion inscripcion = inscripcionRepository.findInscripcionById(idInscripcion);

            if(inscripcion == null || inscripcion.getSocioTitularId() == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            boolean resultado = false;

            // Validar si estamos añadiendo un hijo o un segundo adulto
            Socio socio = socioRepository.findSocioByDNI(dniMiembro);

            if(socio != null) {
                inscripcion.setSegundoAudlto(null);
                resultado = inscripcionRepository.updateInscripcion(inscripcion).equals("EXITO");
            }

            Hijos hijo = hijosRepository.findHijoByDni(dniMiembro);
            if(hijo != null) {
                hijo.setId_inscripcion(0);
                resultado = hijosRepository.updateHijo(hijo) == true;
            }

            inscripcion = inscripcionRepository.findInscripcionById(idInscripcion);
            
            if(resultado == true) {
                return new ResponseEntity<>(inscripcion, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /*
    * 8. Cancelar una inscripción individual o familiar, dado el DNI del socio titular, sin
        borrar a los socios (DELETE)
    */

    @DeleteMapping("/deleteInscripcion")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInscripcion(@RequestBody int idInscripcion) {
        Inscripcion inscripcion = inscripcionRepository.findInscripcionById(idInscripcion);
    }



}