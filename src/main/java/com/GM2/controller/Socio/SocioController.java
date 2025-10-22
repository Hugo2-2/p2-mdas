package com.GM2.controller.Socio;

import com.GM2.model.domain.Socio;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/api/socios")
public class SocioController {

    SocioService socioService;

    public SocioController(SocioService socioService) { this.socioService = socioService; }

    @GetMapping("/addSocio")
    public ModelAndView mostrarFormularioSocio() {
        ModelAndView mv = new ModelAndView("addSocioView");
        mv.addObject("socio", new Socio());
        return mv;
    }


}
