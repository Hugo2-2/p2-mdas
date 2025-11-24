package com.GM2.api;

import com.GM2.model.domain.Inscripcion;
import com.GM2.model.domain.Socio;
import com.GM2.model.repository.HijosRepository;
import com.GM2.model.repository.InscripcionRepository;
import com.GM2.model.repository.SocioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            if(socio.getDni().isEmpty() || socio.getNombre().isEmpty() || socio.getApellidos().isEmpty() || socio.getDireccion().isEmpty() ) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            if(socio.getFechaNacimiento().getYear() > 2007) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

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

            // Crear el objeto Socio
            Socio socio = new Socio();
            socio.setDni(requestBody.getDni());
            socio.setNombre(requestBody.getNombre());
            socio.setApellidos(requestBody.getApellidos());
            socio.setFechaNacimiento(requestBody.getFechaNacimiento());
            socio.setDireccion(requestBody.getDireccion());
            socio.setTieneLicenciaPatron(requestBody.getPatron() != null ? requestBody.getPatron() : false);
            socio.setEsTitular(false); // No es titular, se está añadiendo a una inscripción existente
            socio.setFechaInscripcion(LocalDate.now());

            // Guardar el socio
            String resultado = socioRepository.addSocio(socio);
            
            if(!resultado.equals("EXITO")) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Añadir el socio como segundo adulto a la inscripción
            String resultadoInscripcion = inscripcionRepository.updateInscripcioSinHijos(
                requestBody.getDniTitular(), 
                requestBody.getDni()
            );

            if(!resultadoInscripcion.equals("EXITO")) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(socio, HttpStatus.CREATED);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    

}
