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
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;



/**
 * Controlador REST de la API de inscripciones.
 * Permite realizar operaciones CRUD sobre inscripciones.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
@RestController
@RequestMapping(value = "api/inscripciones", produces = "application/json")
public class InscripcionesRestController {
    InscripcionRepository inscripcionRepository;
    HijosRepository hijosRepository;
    SocioRepository socioRepository;

    /**
     * Constructor de la clase.
     * 
     * @param inscripcionRepository Instancia de InscripcionRepository
     * @param hijosRepository Instancia de HijosRepository
     * @param socioRepository Instancia de SocioRepository
     */
    public InscripcionesRestController(InscripcionRepository inscripcionRepository, HijosRepository hijosRepository, SocioRepository socioRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.hijosRepository = hijosRepository;
        this.socioRepository = socioRepository;


        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.inscripcionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.hijosRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * 1. Obtener la lista de inscripciones individuales (GET)
     * Una inscripción es individual cuando la cuota es igual a 300
     * 
     * @return ResponseEntity con la lista de inscripciones individuales y estado 200 (OK) si se ha podido obtener correctamente
     * y en caso de error: 204 (No Content) o 500 (Internal Server Error)
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
                .filter(inscripcion -> inscripcion.getAnnualFee() == 300)
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


    /**
     * 2. Obtener la lista de inscripciones familiares (GET)
     * Una inscripción es familiar cuando la cuota es mayor que 300
     * 
     * @return ResponseEntity con la lista de inscripciones familiares y estado 200 (OK) si se ha podido obtener correctamente
     * y en caso de error: 204 (No Content) o 500 (Internal Server Error)
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
                .filter(inscripcion -> inscripcion.getAnnualFee() > 300)
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

    /**
     * 3. Obtener la información de una inscripción dado el DNI del socio titular (GET)
     * 
     * @param dniTitular DNI del socio titular
     * @return ResponseEntity con la información de la inscripción y estado 200 (OK) si se ha podido obtener correctamente
     * y en caso de error: 404 (Not Found) o 500 (Internal Server Error)
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

    /**
     * 4. Crear una inscripción para un socio titular (POST)
     * 
     * @param inscripcionBody Cuerpo de la inscripción a crear
     * @return ResponseEntity con la inscripción creada y estado 201 (Created) si se ha podido crear correctamente
     * y en caso de error: 400 (Bad Request), 404 (Not Found), 409 (Conflict) o 500 (Internal Server Error)
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Inscripcion> createInscripcion(@RequestBody Inscripcion inscripcionBody) {
        try {
            // Crear la inscripción
            Inscripcion inscripcion = new Inscripcion(inscripcionBody.getTitularMemberId(), inscripcionBody.getSecondAdult(), inscripcionBody.getChildren());
            
            // Validaciones básicas
            
            if(inscripcion == null || inscripcion.getTitularMemberId() == null || 
               inscripcion.getTitularMemberId().isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            // Verificar que el socio titular existe
            Socio titular = socioRepository.findSocioByDNI(inscripcion.getTitularMemberId());
            if(titular == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Socio no encontrado
            }

            // Verificar que no existe ya una inscripción para este titular
            Inscripcion existente = inscripcionRepository.findInscripcionByDNITitular(inscripcion.getTitularMemberId());
            if(existente != null) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT); // Ya tiene inscripción
            }

            // Intentar crear la inscripción
            boolean resultado = inscripcionRepository.addInscripcion(inscripcion);

            
            if(resultado) {
                titular.setIsTitular(true);
                socioRepository.updateSocio(titular);
                return new ResponseEntity<>(inscripcionRepository.findInscripcionByDNITitular(inscripcion.getTitularMemberId()), HttpStatus.CREATED);
            } else {
                // Falló la creación (puede que ya exista una inscripción para ese titular)
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Clean Code - Reglas de comentarios: Comentario desactualizado sobre Endpoints que no se implementan

    /**
     * 6. Vincular a un nuevo miembro en una inscripción familiar, asumiendo que el nuevo
     * miembro ya se ha registrado previamente como socio (PATCH)
     * 
     * @param idInscripcion Identificador de la inscripción
     * @param dniNuevoMiembro DNI del nuevo miembro a añadir
     * @return ResponseEntity con la inscripción actualizada y estado 200 (OK) si se ha podido actualizar correctamente
     * y en caso de error: 400 (Bad Request), 404 (Not Found) o 500 (Internal Server Error)
     */
    @PatchMapping(value = "/addMiembro/{idInscripcion}")
    public ResponseEntity<Inscripcion> addMiembroAFamiliar( @PathVariable int idInscripcion, @RequestBody String dniNuevoMiembro ) {

        try {
            // Validaciones básicas
            if(dniNuevoMiembro == null || dniNuevoMiembro.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            Inscripcion inscripcion = inscripcionRepository.findInscripcionById(idInscripcion);

            if(inscripcion == null || inscripcion.getTitularMemberId() == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            boolean resInscripcion = false;
            boolean resHijos = false;

            // Validar si estamos añadiendo un hijo o un segundo adulto
            Socio socio = socioRepository.findSocioByDNI(dniNuevoMiembro);
            if(socio != null && !socio.getIsTitular()) {
                inscripcion.setSecondAdult(dniNuevoMiembro);
                inscripcion.setAnnualFee(inscripcion.getAnnualFee() + 250);
            }
            
            Hijos hijo = hijosRepository.findHijoByDni(dniNuevoMiembro);
            if(hijo != null) {

                hijo.setRegistrationId(idInscripcion);
                resHijos = hijosRepository.updateHijo(hijo);
                inscripcion.setAnnualFee(inscripcion.getAnnualFee() + 100);
                
            }

            resInscripcion = inscripcionRepository.updateInscripcion(inscripcion).equals("EXITO");

            if((resHijos && resInscripcion) || (resInscripcion && hijo == null)) {
                return new ResponseEntity<>(inscripcionRepository.findInscripcionById(idInscripcion), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * 7. Desvincular a un miembro de una inscripción familiar, sin borrar al socio (PATCH)
     * 
     * @param idInscripcion Identificador de la inscripción
     * @param dniMiembro DNI del miembro a eliminar
     * @return ResponseEntity con la inscripción actualizada y estado 200 (OK) si se ha podido actualizar correctamente
     * y en caso de error: 400 (Bad Request), 404 (Not Found) o 500 (Internal Server Error)
     */
    @PatchMapping(value = "/removeMiembro/{idInscripcion}")
    public ResponseEntity<Inscripcion> removeMiembroDeFamiliar( @PathVariable int idInscripcion, @RequestBody String dniMiembro ) {

        try {
            if( dniMiembro == null || dniMiembro.isEmpty() ) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            Inscripcion inscripcion = inscripcionRepository.findInscripcionById(idInscripcion);

            if(inscripcion == null || inscripcion.getTitularMemberId() == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            boolean resInscripcion = false;
            boolean resHijo = false;

            // Validar si estamos añadiendo un hijo o un segundo adulto
            Socio socio = socioRepository.findSocioByDNI(dniMiembro);

            if(socio != null) {
                inscripcion.setSecondAdult(null);
                inscripcion.setAnnualFee(inscripcion.getAnnualFee() - 250);

            }

            Hijos hijo = hijosRepository.findHijoByDni(dniMiembro);
            if(hijo != null) {
                hijo.setRegistrationId(0);
                inscripcion.setAnnualFee(inscripcion.getAnnualFee() - 100);
                resHijo = hijosRepository.updateHijo(hijo) == true;
            }

            resInscripcion = inscripcionRepository.updateInscripcion(inscripcion).equals("EXITO");
            inscripcion = inscripcionRepository.findInscripcionById(idInscripcion);
            
            if((resHijo && resInscripcion) || (resInscripcion && hijo == null)) {
                return new ResponseEntity<>(inscripcion, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * 8. Cancelar una inscripción individual o familiar, dado el DNI del socio titular, sin
     * borrar a los socios (DELETE)
     * 
     * @param dniTitular DNI del socio titular
     * @return ResponseEntity con estado 204 (No Content) si se ha podido cancelar correctamente
     * y en caso de error: 400 (Bad Request), 404 (Not Found) o 500 (Internal Server Error)
     */
    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> deleteInscripcion(@PathVariable("dni") String dniTitular) {
        try {
            // Validación
            if(dniTitular == null || dniTitular.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Verificar que la inscripción existe
            Inscripcion inscripcion = inscripcionRepository.findInscripcionByDNITitular(dniTitular);
            if(inscripcion == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Eliminar la inscripción (los socios y los hijos no se eliminan)
            boolean resultado = inscripcionRepository.deleteInscripcionByDniTitular(dniTitular);

            if(resultado) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}