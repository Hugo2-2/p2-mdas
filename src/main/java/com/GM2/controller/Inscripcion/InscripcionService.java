package com.GM2.controller.Inscripcion;

import com.GM2.model.domain.Hijos;
import com.GM2.model.domain.Inscripcion;
import com.GM2.model.repository.HijosRepository;
import com.GM2.model.repository.InscripcionRepository;
import com.GM2.model.repository.SocioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InscripcionService {

    final private InscripcionRepository inscripcionRepository;
    private final HijosRepository hijosRepository;
    private SocioRepository socioRepository;


    public InscripcionService(InscripcionRepository inscripcionRepository, HijosRepository hijosRepository, SocioRepository socioRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.hijosRepository = hijosRepository;
        this.socioRepository = socioRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.inscripcionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.inscripcionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.hijosRepository.setSqlQueriesFileName(sqlQueriesFileName);
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

    public String updateInscripcioConHijos(String dniTitular, List<String> dnisHijos){

        Inscripcion inscripcion = findInscripcionByDNITitular(dniTitular);

        if (inscripcion == null) {
            return "No puedes actualizar la inscripcion porque no existe";
        }

        for (String dni : dnisHijos) {
            if (dni != null && !dni.trim().isEmpty()) {
                Hijos hijo = new Hijos();
                hijo.setDni(dni);
                hijo.setId_inscripcion(inscripcion.getId());

                hijosRepository.addHijo(hijo);
                inscripcion.setCuotaAnual(inscripcion.getCuotaAnual() + 100);
            }
        }

        boolean res = inscripcionRepository.updateInscripcion(inscripcion);

        if(res) {
            return "EXITO";
        } else  {
            return "No se ha podido actualizar la inscripcion";
        }
    }
}
