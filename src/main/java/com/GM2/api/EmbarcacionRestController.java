package com.GM2.api;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.repository.EmbarcacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController()
@RequestMapping(path="api/embarcaciones", produces="application/json")
public class EmbarcacionRestController {
    EmbarcacionRepository embarcacionRepository;

    private static final List<String> TIPOS_VALIDOS = Arrays.asList("VELERO", "YATE", "LANCHA", "BARCA_PESCA", "CATAMARAN");

    public EmbarcacionRestController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";

        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping
    public ResponseEntity<List<Embarcacion>> getAllEmbarcaciones() {
        try {
            List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcaciones();

            if (embarcaciones == null || embarcaciones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(embarcaciones, HttpStatus.OK);

        } catch (Exception e) {
            // Si falla la conexión a la BD o hay otro error inesperado
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Embarcacion> createEmbarcacion(@RequestBody Embarcacion nuevaEmbarcacion) {
        try {
            //No se le debe asociar ningún patron, para cumplir lo que dice el enunciado
            nuevaEmbarcacion.setIdPatron(null);

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

    @PatchMapping(path="/{matricula}", consumes="application/json")
    public ResponseEntity<Embarcacion> updateEmbarcacion(@PathVariable String matricula, @RequestBody Embarcacion nuevaEmbarcacion) {
        try {
            // 1. Buscar si la embarcación existe
            Embarcacion embarcacionActual = embarcacionRepository.findEmbarcacionByMatricula(matricula);
            if (embarcacionActual == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
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

            // B) Actualización de TIPO (Sin validación especial, solo que no sea null)
            if (nuevaEmbarcacion.getTipo() != null) {
                if (!TIPOS_VALIDOS.contains(nuevaEmbarcacion.getTipo())) {
                    return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                embarcacionActual.setTipo(nuevaEmbarcacion.getTipo());
            }

            // C) Validación de PLAZAS (Si se envía un número válido > 0)
            // Nota: Asumimos que si envían 0 o negativo en el JSON es que no quieren actualizarlo o es error.
            // Pero para el PATCH verificamos si se intenta actualizar.
            if (nuevaEmbarcacion.getPlazas() != 0) { // Si viene dato de plazas
                if (nuevaEmbarcacion.getPlazas() < 2) {
                    return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY); // 422
                }
                embarcacionActual.setPlazas(nuevaEmbarcacion.getPlazas());
            }

            // D) Validación de DIMENSIONES
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
}
