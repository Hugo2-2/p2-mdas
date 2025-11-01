package com.GM2.controller.Inscripcion;

import ch.qos.logback.core.model.Model;
import com.GM2.model.domain.Inscripcion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    InscripcionService inscripcionService;

    public InscripcionController(InscripcionService inscripcionService) {  this.inscripcionService = inscripcionService; }

    @GetMapping("/upgradeInscripcion")
    public ModelAndView upgradeInscripcion(@RequestParam("id") int id) {

        Inscripcion inscripcionBase = inscripcionService.findInscripcionById(id);
        ModelAndView modelAndView = new ModelAndView();

        if( inscripcionBase != null ) {
            modelAndView.setViewName("upgradeInscripcion");

            modelAndView.addObject("inscripcion", inscripcionBase);
        }

        return modelAndView;
    }
}
