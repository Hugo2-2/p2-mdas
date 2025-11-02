package com.GM2.controller.Inscripcion;

import com.GM2.model.domain.Inscripcion;
import com.GM2.model.repository.InscripcionRepository;
import com.GM2.model.repository.SocioRepository;
import org.springframework.stereotype.Service;

@Service
public class InscripcionService {

    final private InscripcionRepository inscripcionRepository;
    private SocioRepository socioRepository;


    public InscripcionService(InscripcionRepository inscripcionRepository, SocioRepository socioRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.socioRepository = socioRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.inscripcionRepository.setSqlQueriesFileName(sqlQueriesFileName);
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

    public String updateInscripcioSinHijos(String dniTitular, String dniSegundoAdulto){

        if(socioRepository.findSocioByDNI(dniTitular) == null && socioRepository.findSocioByDNI(dniSegundoAdulto) == null) {
            return "El titular y el segundo adulto no están registrados como socios";
        } else if(socioRepository.findSocioByDNI(dniTitular) == null) {
            return "El titular no está registrado como socio";
        } else if(socioRepository.findSocioByDNI(dniSegundoAdulto) == null) {
            return "El segundo adulto no está registrado como socio.";
        }

        Inscripcion inscripcion = findInscripcionByDNITitular(dniTitular);

        if (inscripcion == null) {
            return "No puedes actualizar la inscripcion porque no existe";
        }
        
        inscripcion.setCuotaAnual(inscripcion.getCuotaAnual() + 250);
        inscripcion.setSegundoAudlto(dniSegundoAdulto);

        boolean res = inscripcionRepository.updateInscripcion(inscripcion);

        if(res) {
            return "EXITO";
        } else  {
            return "No se ha podido actualizar la inscripcion";
        }
    }
}
