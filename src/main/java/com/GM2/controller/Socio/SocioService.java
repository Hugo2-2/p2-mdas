package com.GM2.controller.Socio;

import com.GM2.model.domain.Socio;
import com.GM2.model.repository.SocioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class SocioService {

    private SocioRepository socioRepository;

    public SocioService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
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

        boolean res = socioRepository.addSocio(socio);

        if(res) {
            return "EXITO";
        } else {
            return "No se ha podido guardar el socio";
        }
    }

}
