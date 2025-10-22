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

    public String addSocio(@RequestBody Socio socio) {
        boolean res = socioRepository.addSocio(socio);

        if(res) {
            return "Socio was added successfully";
        } else {
            return "Socio could not be added";
        }
    }

}
