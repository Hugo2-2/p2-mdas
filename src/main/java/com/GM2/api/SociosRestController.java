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

@RestController
@RequestMapping(value = "api/socios", produces = "application/json")
public class SociosRestController {
    SocioRepository socioRepository;
    InscripcionRepository inscripcionRepository;
    HijosRepository hijosRepository;

    public SociosRestController(SocioRepository socioRepository, InscripcionRepository inscripcionRepository, HijosRepository hijosRepository) {
        this.socioRepository = socioRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.hijosRepository = hijosRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.inscripcionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.hijosRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /*
    * 1. Obtener la lista completa de socios (GET)
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

    /*
    * 2. Obtener un información de un socio dado su DNI (GET)
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

    /*
     * 3. Crear socio sin inscripción (POST)
     */

    @PostMapping(value = "/socioSinInscripcion", consumes = "application/json")
    public ResponseEntity<Socio> createSocioSinInscripcion(@RequestBody Socio socio) {

            // Validaciones
            if(socio.getDni().isEmpty() || socio.getNombre().isEmpty() || socio.getApellidos().isEmpty() ) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            if(socio.getFechaNacimiento().getYear() > 2007) {
                Hijos hijo = new Hijos(socio.getDni(), socio.getNombre(), socio.getApellidos(), socio.getFechaNacimiento());
                hijo.setId_inscripcion(0); // Sin inscripción

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
                socio.setEsTitular(false);
                socio.setFechaInscripcion(LocalDate.now());

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
     * 4. Crear socio asociandolo a una inscripción familiar ya existente.
     * 
     * @param requestBody Objeto que contiene los datos del socio y el DNI del titular
     * @return ResponseEntity con el socio creado o error
     */
    @PostMapping(value = "/socioConInscripcion", consumes = "application/json")
    public ResponseEntity<Socio> createSocioConInscripcion(@RequestBody SocioConInscripcionRequest requestBody) {
        
        // Validaciones de los campos del socio
        if(requestBody.getDni() == null || requestBody.getDni().isEmpty() || 
           requestBody.getNombre() == null || requestBody.getNombre().isEmpty() || 
           requestBody.getApellidos() == null || requestBody.getApellidos().isEmpty() || 
           requestBody.getDireccion() == null || requestBody.getDireccion().isEmpty() ||
           requestBody.getFechaNacimiento() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        // Validación del DNI del titular
        if(requestBody.getDniTitular() == null || requestBody.getDniTitular().isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        try {
            // Verificar que la inscripción existe
            Inscripcion inscripcion = inscripcionRepository.findInscripcionByDNITitular(requestBody.getDniTitular());
            
            if(inscripcion == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            Socio socio = new Socio(requestBody.getNombre(), requestBody.getApellidos(), requestBody.getDni(), requestBody.getFechaNacimiento(), requestBody.getDireccion(), LocalDate.now(), requestBody.getPatron());


            if(socio.getFechaNacimiento().getYear() > 2007) {
                Hijos hijo = new Hijos(socio.getDni(), socio.getNombre(), socio.getApellidos(), socio.getFechaNacimiento());
                hijo.setId_inscripcion(inscripcion.getId()); // Inscripción existente

                try {
                    Boolean res = hijosRepository.addHijo(hijo) == true;

                    inscripcion.setCuotaAnual(inscripcion.getCuotaAnual() + 100);

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

                if(inscripcion.getSegundoAudlto() == null)
                    inscripcion.setCuotaAnual(inscripcion.getCuotaAnual() + 250);

                inscripcion.setSegundoAudlto(socio.getDni());

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

    /*
    * 5. Actualizar los campos de información de un socio, excepto el DNI (PATCH)
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
                socio.setNombre((String) updates.get("nombre"));
            }
            if(updates.containsKey("apellidos")) {
                socio.setApellidos((String) updates.get("apellidos"));
            }
            if(updates.containsKey("fechaNacimiento")) {
                socio.setFechaNacimiento(LocalDate.parse((String) updates.get("fechaNacimiento")));
            }
            if(updates.containsKey("direccion")) {
                socio.setDireccion((String) updates.get("direccion"));
            }
            if(updates.containsKey("fechaInscripcion")) {
                socio.setFechaInscripcion(LocalDate.parse((String) updates.get("fechaInscripcion")));
            }
            if(updates.containsKey("esTitular")) {
                socio.setEsTitular((Boolean) updates.get("esTitular"));
            }
            if(updates.containsKey("tieneLicenciaPatron")) {
                socio.setTieneLicenciaPatron((Boolean) updates.get("tieneLicenciaPatron"));
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

    /*
    * 6. Eliminar a un socio si no está vinculado a ninguna inscripción (DELETE)
    * Busca en la tabla de socios y en la tabla de hijos
    */
    @DeleteMapping
    public ResponseEntity<Void> deleteSocio(@RequestBody String dni) {
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
                if(socio.getEsTitular()) {
                    Inscripcion inscripcion = inscripcionRepository.findInscripcionByDNITitular(dni);
                    if(inscripcion != null) {
                        return new ResponseEntity<>(HttpStatus.CONFLICT);
                    }
                }

                // Verificar si es segundo adulto en alguna inscripción
                List<Inscripcion> todasInscripciones = inscripcionRepository.findAllInscripciones();
                if(todasInscripciones != null) {
                    for(Inscripcion insc : todasInscripciones) {
                        if(insc.getSegundoAudlto() != null && insc.getSegundoAudlto().equals(dni)) {
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
                if(hijo.getId_inscripcion() > 0) {
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
