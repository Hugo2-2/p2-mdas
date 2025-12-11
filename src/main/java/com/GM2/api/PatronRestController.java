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

/**
 * Controlador REST para la gestión de recursos de tipo Patron.
 * Permite realizar operaciones CRUD, así como gestionar la vinculación
 * y desvinculación de patrones con las embarcaciones.
 */
@RestController()
@RequestMapping(path="api/patrones", produces="application/json")
public class PatronRestController {
    private final EmbarcacionRepository embarcacionRepository;
    PatronRepository patronRepository;

    /**
     * Constructor del controlador.
     * Inyecta los repositorios necesarios y configura la carga de sentencias SQL.
     *
     * @param patronRepository Repositorio para la gestión de patrones.
     * @param embarcacionRepository Repositorio para consultar estados de embarcaciones.
     */
    public PatronRestController(PatronRepository patronRepository, EmbarcacionRepository embarcacionRepository) {
        this.patronRepository = patronRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";

        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.embarcacionRepository = embarcacionRepository;
    }

    /**
     * Obtiene el listado completo de todos los patrones registrados.
     *
     * @return ResponseEntity con la lista de patrones y estado 200 OK,
     * o 404 Not Found si no se encuentran datos.
     */
    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrones() {
        try {
            List<Patron> patrones = patronRepository.findAllPatrones();

            if (patrones == null || patrones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(patrones, HttpStatus.OK);

        } catch (Exception e) {
            // Si falla la conexión a la BD o hay otro error inesperado
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Registra un nuevo patrón en el sistema.
     * Realiza validaciones de campos obligatorios, unicidad de DNI y coherencia de fechas.
     *
     * @param nuevoPatron Objeto con los datos del nuevo patrón.
     * @return ResponseEntity con el patrón creado y estado 201 Created,
     * o códigos de error (400, 409, 422) si fallan las validaciones.
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Patron> createPatron(@RequestBody Patron nuevoPatron) {

        try {
            // Verificamos que no queden campos nulos (Campos obligatorios NOT NULL)
            if (nuevoPatron.getDni() == null || nuevoPatron.getDni().trim().isEmpty() ||
                    nuevoPatron.getNombre() == null || nuevoPatron.getNombre().trim().isEmpty() ||
                    nuevoPatron.getApellidos() == null || nuevoPatron.getApellidos().trim().isEmpty() ||
                    nuevoPatron.getFechaNacimiento() == null ||
                    nuevoPatron.getFechaExpedicionTitulo() == null) {

                // 400 Bad Request: El cliente envió datos incompletos
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            // Validar si el DNI ya está registrado (Unicidad)
            if (patronRepository.isRegistered(nuevoPatron.getDni())) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            // Validar lógica de fechas: No puede haber nacido en el futuro
            if (nuevoPatron.getFechaNacimiento().isAfter(java.time.LocalDate.now())) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            // Validar lógica de fechas: Título no puede ser futuro ni anterior al nacimiento
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
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza la información personal de un patrón existente.
     * No permite modificar el DNI. Incluye validaciones de fechas.
     *
     * @param dni Identificador del patrón a modificar.
     * @param newPatron Objeto con los nuevos datos.
     * @return ResponseEntity con el patrón actualizado y estado 200 OK.
     */
    @PatchMapping(path="/{dni}", consumes="application/json")
    public ResponseEntity<Patron> updatePatron(@PathVariable("dni") String dni, @RequestBody Patron newPatron) {

        Patron patronActual = patronRepository.findPatronByDNI(dni);
        if (patronActual == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        // Actualización parcial de campos
        if (newPatron.getNombre() != null) patronActual.setNombre(newPatron.getNombre());
        if (newPatron.getApellidos() != null) patronActual.setApellidos(newPatron.getApellidos());
        if (newPatron.getFechaNacimiento() != null) patronActual.setFechaNacimiento(newPatron.getFechaNacimiento());
        if (newPatron.getFechaExpedicionTitulo() != null) patronActual.setFechaExpedicionTitulo(newPatron.getFechaExpedicionTitulo());

        // Validaciones de negocio tras la actualización
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

    /**
     * Vincula un patrón a una embarcación.
     * Verifica que el patrón exista y no esté ocupado en otra embarcación.
     *
     * @param matricula Matrícula de la embarcación (en la URL).
     * @param dniPatron DNI del patrón a asignar (en el cuerpo de la petición).
     * @return ResponseEntity con el objeto Patron asignado.
     */
    @PatchMapping(path="/{matricula}/patron")
    public ResponseEntity<Patron> assignPatronToEmbarcacion(@PathVariable String matricula, @RequestBody String dniPatron) {
        // Limpiamos el dni de posibles comillas del JSON
        String dniPatronLimpio = dniPatron.replaceAll("[\"{}]", "").trim();

        if(embarcacionRepository.findEmbarcacionByMatricula(matricula) == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Patron patron = patronRepository.findPatronByDNI(dniPatronLimpio);
        if(patron == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        // Comprobar que el patrón no está asignado a ninguna otra embarcación
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

    /**
     * Desvincula un patrón de una embarcación.
     * Verifica que el patrón indicado sea el que realmente está asignado a la embarcación.
     *
     * @param matricula Matrícula de la embarcación (en la URL).
     * @param dniPatron DNI del patrón a retirar (en el cuerpo de la petición).
     * @return ResponseEntity con el objeto Patron desvinculado.
     */
    @PatchMapping(path="/{matricula}/noPatron")
    public ResponseEntity<Patron> unassignPatronToEmbarcacion(@PathVariable String matricula, @RequestBody String dniPatron) {
        // Limpiamos el dni
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

        // Seguridad: Si el barco está vacío O el patrón no coincide, damos error para evitar borrados accidentales
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

    /**
     * Elimina un patrón del sistema.
     * Solo se permite si el patrón no está asignado actualmente a ninguna embarcación.
     *
     * @param dni Identificador del patrón a borrar.
     * @return ResponseEntity con estado 204 No Content si se borra correctamente.
     */
    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> deletePatron(@PathVariable String dni) {

        // Validar existencia del patrón
        if (patronRepository.findPatronByDNI(dni) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Validar integridad referencial: ¿Tiene barco asignado?
        if (embarcacionRepository.isPatronAssignedToEmbarcacion(dni)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // Ejecutar borrado físico
        boolean exito = patronRepository.deletePatron(dni);

        if (exito) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtener un patrón específico por su DNI.
     * Endpoint auxiliar, utilizado principalmente para verificaciones de clientes y pruebas.
     *
     * @param dni Identificador único del patrón.
     * @return ResponseEntity con el patrón encontrado y estado 200 OK.
     */
    @GetMapping("/{dni}")
    public ResponseEntity<Patron> getPatronByDni(@PathVariable String dni) {
        Patron patron = patronRepository.findPatronByDNI(dni);

        if (patron != null) {
            return new ResponseEntity<>(patron, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}