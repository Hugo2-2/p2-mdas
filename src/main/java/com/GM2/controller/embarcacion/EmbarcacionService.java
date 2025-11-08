package com.GM2.controller.embarcacion;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.PatronRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Capa de servicio para la lógica de negocio de las Embarcaciones.
 * * Esta clase gestiona las operaciones y validaciones de la flota del club,
 * como la creación de embarcaciones y la consulta de las mismas,
 * asegurando que se cumplen las reglas de negocio antes de
 * interactuar con el repositorio.
 * * @author gm2equipo1
 * @version 1.0
 */

@Service
public class EmbarcacionService {
    EmbarcacionRepository embarcacionRepository;
    PatronRepository patronRepository;

    /**
     * Constructor para la inyección de dependencias de los repositorios.
     * * @param patronRepository Repositorio para acceder a los datos de patrones.
     * @param embarcacionRepository Repositorio para acceder a los datos de embarcaciones.
     */
    public EmbarcacionService(PatronRepository patronRepository, EmbarcacionRepository embarcacionRepository) {
        this.patronRepository = patronRepository;
        this.embarcacionRepository = embarcacionRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Busca y devuelve todas las embarcaciones de un tipo específico.
     * * @param tipo El tipo de embarcación a buscar (ej. "VELERO", "YATE").
     * @return Una lista de objetos {@link Embarcacion} que coinciden con el tipo.
     */
    public List<Embarcacion> findAllEmbarcacionesByTipo(String tipo){ return embarcacionRepository.findAllEmbarcacionesByTipo(tipo);}


    /**
     * Valida y añade una nueva embarcación a la base de datos.
     * Realiza múltiples comprobaciones de negocio antes de la inserción.
     * * @param embarcacion El objeto {@link Embarcacion} con los datos a insertar.
     * @return Un String que indica "EXITO" si la operación fue correcta,
     * o un mensaje de error específico si falló la validación.
     */
    public String addEmbarcacion(Embarcacion embarcacion){

        if(embarcacionRepository.findEmbarcacionByMatricula(embarcacion.getMatricula()) != null){
            return "Error: Ya existe una embarcación con esa matrícula";
        }

        if(embarcacionRepository.findEmbarcacionByNombre(embarcacion.getNombre()) != null){
            return "Error: Ya existe una embarcación con dicho nombre asociado";
        }

        if(embarcacion.getPlazas() < 2) {
            return "Error: El número mínimo de plazas debe ser 2";
        }

        try {
            //Convertir el String a un número (double es más seguro para dimensiones)
            double dimensiones = Double.parseDouble(embarcacion.getDimensiones());

            if (dimensiones < 1) {
                return "Error: La embarcación debe tener al menos 1m2";
            }

        } catch (NumberFormatException e) {
            return "Error: El formato de las dimensiones no es válido.";
        }

        if(patronRepository.findPatronByDNI(embarcacion.getIdPatron()) == null){
            return "Error: El patron no existe";
        }

        if(embarcacionRepository.isPatronAssignedToEmbarcacion(embarcacion.getIdPatron())) {
            return "Error: El patron ya se encuentra asignado a una embarcación";
        }

        Boolean resultado = embarcacionRepository.addEmbarcacion(embarcacion);
        if(!resultado){
            return "Error: No se pudo añadir la embarcación";
        }

        return "EXITO";
    }
}
