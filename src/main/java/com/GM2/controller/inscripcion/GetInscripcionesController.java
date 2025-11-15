package com.GM2.controller.inscripcion;

import com.GM2.model.domain.Inscripcion;
import com.GM2.model.repository.InscripcionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/api/inscripciones")
public class GetInscripcionesController {

    InscripcionRepository inscripcionRepository;

    public GetInscripcionesController(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.inscripcionRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping("/")
    public ModelAndView getInscripciones(){
        ModelAndView mv = new ModelAndView("inscripcion/listInscripcionesView");

        List<Inscripcion> inscripciones = inscripcionRepository.findAllInscripciones();

        mv.addObject("inscripciones", inscripciones);

        return mv;
    }
}
