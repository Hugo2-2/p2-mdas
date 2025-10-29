package com.GM2.controller.Inscripcion;

import com.GM2.model.domain.Inscripcion;
import com.GM2.model.repository.InscripcionRepository;
import org.springframework.stereotype.Service;

@Service
public class InscripcionService {

    final private InscripcionRepository inscripcionRepository;

    public InscripcionService(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.inscripcionRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    public Inscripcion findInscripcionById(int id){
        return inscripcionRepository.findInscripcionById(id);
    }

    public Inscripcion findInscripcionByDNITitular(String DNI){
        return inscripcionRepository.findInscripcionByDNITitular(DNI);
    }

    public Boolean addInscripcion(Inscripcion inscripcion){

        if( inscripcion == null ) return false;

        if( findInscripcionByDNITitular(inscripcion.getSocioTitularId()) != null ){
            return false;
        }

        return inscripcionRepository.addInscripcion(inscripcion);


    }

    public String updateInscripcion(Inscripcion inscripcion){
        if( inscripcion == null ) return "No se ha podido ingresar la inscripcion";

        if( findInscripcionByDNITitular(inscripcion.getSocioTitularId()) == null ){
            return "No puedes actualizar la inscripcion porque no existe";
        }

        boolean res = inscripcionRepository.updateInscripcion(inscripcion);

        if(res) {
            return "EXITO";
        } else  {
            return "No se ha podido actualizar la inscripcion";
        }
    }
}
