package com.GM2.controller.Socio;

import com.GM2.controller.Inscripcion.InscripcionService;
import com.GM2.model.domain.Inscripcion;
import com.GM2.model.domain.Socio;
import com.GM2.model.repository.InscripcionRepository;
import com.GM2.model.repository.SocioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;

@Service
public class SocioService {

    final private SocioRepository socioRepository;
    final private InscripcionService inscripcionService;

    public SocioService(SocioRepository socioRepository, InscripcionService inscripcionService) {
        this.socioRepository = socioRepository;
        this.inscripcionService = inscripcionService;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    public List<Socio> findAll() { return socioRepository.findAllSocios(); }

    public Socio findById(String dni) {
        return socioRepository.findSocioByDNI(dni);
    }

    public String addSocio(Socio socio) {
        if( socio == null ) return "No se ha ingresado un socio";
        if(socio.getFechaNacimiento().getYear() > 2007) return "Debes de ser mayor de edad para realizar esta inscripción";

        boolean resSocio = socioRepository.addSocio(socio);

        // Creamos su inscripcion simple que posteriormente podrá se ampliada
        if(socio.getEsTitular()) {

            Inscripcion inscripcion = new Inscripcion(socio.getDni());

            boolean resInscripcion = inscripcionService.addInscripcion(inscripcion);

            if( resSocio & resInscripcion ) {
                return "EXITO";
            } else {
                return "No se ha podido guardar el socio";
            }
        }


        if( resSocio ) {
            return "EXITO";
        } else {
            return "No se ha podido guardar el socio";
        }
    }

}
