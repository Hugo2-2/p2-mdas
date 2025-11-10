package com.GM2.controller.Inscripcion;

import com.GM2.model.domain.Hijos;
import com.GM2.model.domain.Inscripcion;
import com.GM2.model.repository.HijosRepository;
import com.GM2.model.repository.InscripcionRepository;
import com.GM2.model.repository.SocioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
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

        inscripcion.setFechaCreacion();

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

        if(!socioRepository.findSocioByDNI(dniTitular).getEsTitular()) {
            return "El socio introducido como titular no es titular de ninguna inscripción";
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

    public String updateInscripcioConHijos(String dniTitular, List<String> dnisHijos, List<String> nombreHijos, List<String> apellidosHijos, List<LocalDate> fechaNacimientoHijos){
        Inscripcion inscripcion = findInscripcionByDNITitular(dniTitular);

        if (inscripcion == null) {
            return "No puedes actualizar la inscripcion porque no existe";
        }

        for (int i = 0; i < dnisHijos.size(); i++) {

            String dni = dnisHijos.get(i);

            // Solo procesamos si el DNI no está vacío
            if (dni != null && !dni.trim().isEmpty()) {

                Hijos hijo = new Hijos();
                hijo.setDni(dni);
                hijo.setNombre(nombreHijos.get(i));
                hijo.setApellidos(apellidosHijos.get(i));

                // Asignamos el ID de la inscripción principal
                hijo.setId_inscripcion(inscripcion.getId());

                // Parseamos y validamos la fecha
                try {
                    hijo.setFechaNacimiento(fechaNacimientoHijos.get(i));
                } catch (DateTimeParseException e) {
                    return "Error: El formato de fecha del hijo " + (i + 1) + " no es válido (use AAAA-MM-DD).";
                }

                // Guardamos el hijo completo en la base de datos
                hijosRepository.addHijo(hijo);

                // Actualizamos la cuota en el objeto de inscripción (100€ por hijo)
                inscripcion.setCuotaAnual(inscripcion.getCuotaAnual() + 100);
            }
        }

        boolean res = inscripcionRepository.updateInscripcion(inscripcion);

        if(res) {
            return "EXITO";
        } else  {
            return "No se ha podido actualizar la cuota de la inscripcion";
        }    
    }
}
