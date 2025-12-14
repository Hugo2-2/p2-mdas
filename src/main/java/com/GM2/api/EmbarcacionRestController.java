package com.GM2.api;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.repository.EmbarcacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Controlador REST para la gestión de recursos de tipo Embarcacion.
 * Expone los endpoints para realizar operaciones CRUD sobre la flota.
 *
 * @author gm2equipo1
 * @version 1.0
 */
@RestController()
@RequestMapping(path="api/embarcaciones", produces="application/json")
public class EmbarcacionRestController {
    EmbarcacionRepository embarcacionRepository;

    // Lista de tipos de embarcación permitidos para las validaciones
    private static final List<String> TIPOS_VALIDOS = Arrays.asList("VELERO", "YATE", "LANCHA", "BARCA_PESCA", "CATAMARAN");

    /**
     * Constructor del controlador.
     * Inyecta el repositorio y carga el fichero de propiedades SQL.
     *
     * @param embarcacionRepository Repositorio de datos de embarcaciones.
     */
    public EmbarcacionRestController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";

        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Obtiene el listado completo de todas las embarcaciones.
     *
     * @return ResponseEntity con la lista de embarcaciones y estado 200 OK,
     * o 404 Not Found si la lista está vacía.
     */
    @GetMapping
    public ResponseEntity<List<Embarcacion>> getAllEmbarcaciones() {
        try {
            List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcaciones();

            if (embarcaciones == null || embarcaciones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(embarcaciones, HttpStatus.OK);

        } catch (Exception e) {
            // Si falla la conexión a la BD o hay otro error inesperado
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene un listado de embarcaciones filtrado por su tipo.
     *
     * @param tipo El tipo de embarcación a buscar (ej. VELERO).
     * @return ResponseEntity con la lista filtrada y estado 200 OK,
     * o 204 No Content si no hay coincidencias.
     */
    @GetMapping(params = "tipo")
    public ResponseEntity<List<Embarcacion>> getEmbarcacionesByTipo(String tipo) {
        try {
            List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcacionesByTipo(tipo);

            if (embarcaciones == null || embarcaciones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(embarcaciones, HttpStatus.OK);

        } catch (Exception e) {
            // Si falla la conexión a la BD o hay otro error inesperado
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Crea una nueva embarcación en el sistema.
     * Realiza validaciones de campos obligatorios y unicidad de matrícula y nombre.
     *
     * @param nuevaEmbarcacion Objeto con los datos de la nueva embarcación.
     * @return ResponseEntity con la embarcación creada y estado 201 Created,
     * o códigos de error 400 (Bad Request) o 422 (Unprocessable Entity).
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Embarcacion> createEmbarcacion(@RequestBody Embarcacion nuevaEmbarcacion) {
        try {

            // Verificar que los datos obligatorios no sean nulos ni estén vacíos
            if (nuevaEmbarcacion.getMatricula() == null || nuevaEmbarcacion.getMatricula().trim().isEmpty() ||
                    nuevaEmbarcacion.getNombre() == null || nuevaEmbarcacion.getNombre().trim().isEmpty() ||
                    nuevaEmbarcacion.getTipo() == null || nuevaEmbarcacion.getTipo().trim().isEmpty() ||
                    nuevaEmbarcacion.getDimensiones() == null || nuevaEmbarcacion.getDimensiones().trim().isEmpty() ||
                    nuevaEmbarcacion.getPlazas() <= 0) {

                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            // No se le debe asociar ningún patron al crear, para cumplir lo que dice el enunciado
            nuevaEmbarcacion.setIdPatron(null);

            // Validar la duplicidad de datos (Matrícula y Nombre deben ser únicos)
            if (embarcacionRepository.findEmbarcacionByMatricula(nuevaEmbarcacion.getMatricula()) != null) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            if (embarcacionRepository.findEmbarcacionByNombre(nuevaEmbarcacion.getNombre()) != null) {
                return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            boolean exito = embarcacionRepository.addEmbarcacion(nuevaEmbarcacion);

            if (exito) {
                return new ResponseEntity<>(nuevaEmbarcacion, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza los datos de una embarcación existente.
     * Permite modificar nombre, tipo, plazas y dimensiones.
     *
     * @param matricula Identificador de la embarcación a modificar.
     * @param nuevaEmbarcacion Objeto con los nuevos datos a actualizar.
     * @return ResponseEntity con la embarcación actualizada y estado 200 OK,
     * o códigos de error correspondientes si fallan las validaciones.
     */
    @PatchMapping(path="/{matricula}", consumes="application/json")
    public ResponseEntity<Embarcacion> updateEmbarcacion(@PathVariable String matricula, @RequestBody Embarcacion nuevaEmbarcacion) {
        try {
            // 1. Buscar si la embarcación existe
            Embarcacion embarcacionActual = embarcacionRepository.findEmbarcacionByMatricula(matricula);
            if (embarcacionActual == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            // 2. Comprobar que no se quiera cambiar la matricula
            if(nuevaEmbarcacion.getMatricula() != null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            // --- VALIDACIONES Y ACTUALIZACIÓN DE CAMPOS ---

            // A) Validación de NOMBRE (Si se envía y es diferente al actual)
            if (nuevaEmbarcacion.getNombre() != null && !nuevaEmbarcacion.getNombre().equals(embarcacionActual.getNombre())) {
                // Comprobamos si el nuevo nombre ya está usado por OTRA embarcación
                Embarcacion otraEmbarcacion = embarcacionRepository.findEmbarcacionByNombre(nuevaEmbarcacion.getNombre());
                if (otraEmbarcacion != null) {
                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                }
                embarcacionActual.setNombre(nuevaEmbarcacion.getNombre());
            }

            // B) Actualización de TIPO (Debe ser un tipo válido de la lista predefinida)
            if (nuevaEmbarcacion.getTipo() != null) {
                if (!TIPOS_VALIDOS.contains(nuevaEmbarcacion.getTipo())) {
                    return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                embarcacionActual.setTipo(nuevaEmbarcacion.getTipo());
            }

            // C) Validación de PLAZAS (Debe ser un número positivo mayor o igual a 2)
            if (nuevaEmbarcacion.getPlazas() != 0) { // Si viene dato de plazas
                if (nuevaEmbarcacion.getPlazas() < 2) {
                    return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                embarcacionActual.setPlazas(nuevaEmbarcacion.getPlazas());
            }

            // D) Validación de DIMENSIONES (Formato numérico válido y mayor que 1)
            if (nuevaEmbarcacion.getDimensiones() != null) {
                try {
                    // Intentamos parsear para validar formato y lógica
                    double dimensionesNum = Double.parseDouble(nuevaEmbarcacion.getDimensiones());
                    if (dimensionesNum < 1) {
                        return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                    embarcacionActual.setDimensiones(nuevaEmbarcacion.getDimensiones());
                } catch (NumberFormatException e) {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // 400
                }
            }

            // E) Guardar cambios en base de datos
            boolean exito = embarcacionRepository.updateEmbarcacion(embarcacionActual);

            if (exito) {
                return new ResponseEntity<>(embarcacionActual, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Elimina una embarcación del sistema.
     * Solo se permite el borrado si la embarcación no tiene dependencias (alquileres o reservas).
     *
     * @param matricula Identificador de la embarcación a eliminar.
     * @return ResponseEntity con estado 204 No Content si se borra,
     * o 409 Conflict si no se puede borrar por integridad referencial.
     */
    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> deleteEmbarcacion(@PathVariable String matricula) {
        // Validar existencia de la embarcacion
        if (embarcacionRepository.findEmbarcacionByMatricula(matricula) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Validar que la embarcacion no este alquilada (Integridad referencial)
        if (embarcacionRepository.isEmbarcacionAlquilada(matricula)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // Validar que la embarcacion no este reservada (Integridad referencial)
        if (embarcacionRepository.isEmbarcacionReservada(matricula)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // Ejecutar borrado físico
        boolean exito = embarcacionRepository.deleteEmbarcacion(matricula);

        if (exito) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtener una embarcación específica por su matrícula.
     * Método auxiliar utilizado principalmente para verificar cambios en las pruebas.
     *
     * @param matricula Identificador único de la embarcación.
     * @return ResponseEntity con la embarcación encontrada y estado 200 OK,
     * o 404 Not Found si no existe.
     */
    @GetMapping("/{matricula}")
    public ResponseEntity<Embarcacion> getEmbarcacionByMatricula(@PathVariable String matricula) {
        Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(matricula);

        if (embarcacion != null) {
            return new ResponseEntity<>(embarcacion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}