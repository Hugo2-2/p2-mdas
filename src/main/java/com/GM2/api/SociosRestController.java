package com.GM2.api;

import com.GM2.model.domain.Hijos;
import com.GM2.model.domain.Inscripcion;
import com.GM2.model.domain.Socio;
import com.GM2.model.domain.SocioConInscripcionRequest;
import com.GM2.model.repository.HijosRepository;
import com.GM2.model.repository.InscripcionRepository;
import com.GM2.model.repository.SocioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.time.LocalDate;
import java.util.List;

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

            if( socios == null || socios.isEmpty() ) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            /*A lo mejor hay que retornar tambien a los hijos*/

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

            if(socio == null ) {
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
            if(socio.getNationalId().isEmpty() || socio.getName().isEmpty() || socio.getSurname().isEmpty() ) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            if(socio.getBirthDate().getYear() > 2007) {
                Hijos hijo = new Hijos(socio.getNationalId(), socio.getName(), socio.getSurname(), socio.getBirthDate());
                hijo.setRegistrationId(0); // Sin inscripción

                try {
                    Boolean res = hijosRepository.addHijo(hijo) == true;

                    if(res) {
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
                    Boolean res = socioRepository.addSocio(socio).equals("EXITO");

                    if(res) {
                        return new ResponseEntity<>(socio, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } catch (Exception e) {
                    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            
    }

    /**
     * 4. Crear socio asociandolo a una inscripción familiar ya existente (POST)
     * 
     * @param requestBody Objeto que contiene los datos del socio y el DNI del titular
     * @return ResponseEntity con el socio creado y estado 201 (Created) si se ha podido crear correctamente
     * y en caso de error: 400 (Bad Request), 404 (Not Found) o 500 (Internal Server Error)
     */
    @PostMapping(value = "/socioConInscripcion", consumes = "application/json")
    public ResponseEntity<Socio> createSocioConInscripcion(@RequestBody SocioConInscripcionRequest requestBody) {
        
        // Validaciones de los campos del socio
        if(requestBody.getNationalId() == null || requestBody.getNationalId().isEmpty() || 
           requestBody.getName() == null || requestBody.getName().isEmpty() || 
           requestBody.getSurname() == null || requestBody.getSurname().isEmpty() || 
           requestBody.getAddress() == null || requestBody.getAddress().isEmpty() ||
           requestBody.getBirthDate() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        // Validación del DNI del titular
        if(requestBody.getTitularNationalId() == null || requestBody.getTitularNationalId().isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        try {
            // Verificar que la inscripción existe
            Inscripcion inscripcion = inscripcionRepository.findInscripcionByDNITitular(requestBody.getTitularNationalId());
            
            if(inscripcion == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            Socio socio = new Socio(requestBody.getName(), requestBody.getSurname(), requestBody.getNationalId(), requestBody.getBirthDate(), requestBody.getAddress(), LocalDate.now(), requestBody.getIsSkipper());


            if(socio.getBirthDate().getYear() > 2007) {
                Hijos hijo = new Hijos(socio.getNationalId(), socio.getName(), socio.getSurname(), socio.getBirthDate());
                hijo.setRegistrationId(inscripcion.getId()); // Inscripción existente

                try {
                    Boolean res = hijosRepository.addHijo(hijo) == true;

                    inscripcion.setAnnualFee(inscripcion.getAnnualFee() + 100);

                    if(!res) {
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } catch (Exception e) {
                    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {

                // Guardar el socio
                String resultado = socioRepository.addSocio(socio);
                
                if(!resultado.equals("EXITO")) {
                    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                if(inscripcion.getSecondAdult() == null)
                    inscripcion.setAnnualFee(inscripcion.getAnnualFee() + 250);

                inscripcion.setSecondAdult(socio.getNationalId());

            }

            // Añadir el socio como segundo adulto a la inscripción
            Boolean resultadoInscripcion = inscripcionRepository.updateInscripcion(inscripcion).equals("EXITO");

            if(!resultadoInscripcion) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(socio, HttpStatus.CREATED);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
            // Buscar el socio existente
            Socio socio = socioRepository.findSocioByDNI(dni);
            
            if(socio == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Aplicar las actualizaciones parciales
            if(updates.containsKey("nombre")) {
                socio.setName((String) updates.get("nombre"));
            }
            if(updates.containsKey("apellidos")) {
                socio.setSurname((String) updates.get("apellidos"));
            }
            if(updates.containsKey("fechaNacimiento")) {
                socio.setBirthDate(LocalDate.parse((String) updates.get("fechaNacimiento")));
            }
            if(updates.containsKey("direccion")) {
                socio.setAddress((String) updates.get("direccion"));
            }
            if(updates.containsKey("fechaInscripcion")) {
                socio.setRegistrationDate(LocalDate.parse((String) updates.get("fechaInscripcion")));
            }
            if(updates.containsKey("esTitular")) {
                socio.setIsTitular((Boolean) updates.get("esTitular"));
            }
            if(updates.containsKey("tieneLicenciaPatron")) {
                socio.setHasSkipperLicense((Boolean) updates.get("tieneLicenciaPatron"));
            }

            // Guardar los cambios
            String resultado = socioRepository.updateSocio(socio);
            
            if(resultado.equals("EXITO")) {
                return new ResponseEntity<>(socio, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
            // Validación
            if(dni == null || dni.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Primero verificar si existe como socio
            Socio socio = socioRepository.findSocioByDNI(dni);
            
            if(socio != null) {
                // Es un socio adulto - verificar que no esté vinculado a inscripciones
                
                // Verificar si es titular de alguna inscripción
                if(socio.getIsTitular()) {
                    Inscripcion inscripcion = inscripcionRepository.findInscripcionByDNITitular(dni);
                    if(inscripcion != null) {
                        return new ResponseEntity<>(HttpStatus.CONFLICT);
                    }
                }

                // Verificar si es segundo adulto en alguna inscripción
                List<Inscripcion> todasInscripciones = inscripcionRepository.findAllInscripciones();
                if(todasInscripciones != null) {
                    for(Inscripcion insc : todasInscripciones) {
                        if(insc.getSecondAdult() != null && insc.getSecondAdult().equals(dni)) {
                            return new ResponseEntity<>(HttpStatus.CONFLICT);
                        }
                    }
                }

                // Si pasa las validaciones, eliminar el socio
                boolean resultado = socioRepository.deleteSocio(dni);
                if(resultado) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            
            // Si no es socio, verificar si existe como hijo
            Hijos hijo = hijosRepository.findHijoByDni(dni);
            
            if(hijo != null) {
                // Es un hijo - verificar que no esté vinculado a una inscripción (id_inscripcion > 0)
                if(hijo.getRegistrationId() > 0) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }

                // Si no está vinculado, eliminar el hijo
                boolean resultado = hijosRepository.deleteHijo(dni);
                if(resultado) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            // Si no existe ni como socio ni como hijo
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
