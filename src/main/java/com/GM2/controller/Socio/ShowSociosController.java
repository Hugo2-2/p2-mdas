package com.GM2.controller.Socio;

import com.GM2.model.domain.Socio;
import com.GM2.model.repository.SocioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/api/socios")
public class ShowSociosController {
    SocioRepository socioRepository;

    public ShowSociosController(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping("/")
    public ModelAndView getSocios() {
        List<Socio> socios = socioRepository.findAllSocios();

        ModelAndView mv = new ModelAndView("listSocios");

        mv.addObject("socios", socios);

        return mv;
    }
}
