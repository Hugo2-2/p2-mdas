package com.GM2.controller.embarcacion;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Patron;
import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.PatronRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbarcacionService {
    EmbarcacionRepository embarcacionRepository;
    PatronRepository patronRepository;

    public EmbarcacionService(PatronRepository patronRepository, EmbarcacionRepository embarcacionRepository) {
        this.patronRepository = patronRepository;
        this.embarcacionRepository = embarcacionRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    public List<Embarcacion> findAllEmbarcacionesByTipo(String tipo){ return embarcacionRepository.findAllEmbarcacionesByTipo(tipo);}

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
        if(!embarcacionRepository.isPatronAssignedToEmbarcacion(embarcacion.getIdPatron())) {
            return "Error: El patron ya se encuentra asignado a una embarcación";
        }

        Boolean resultado = embarcacionRepository.addEmbarcacion(embarcacion);
        if(!resultado){
            return "Error: No se pudo añadir la embarcación";
        }

        return "EXITO";
    }
}
