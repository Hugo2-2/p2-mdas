package com.GM2.controller.embarcacion;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.repository.EmbarcacionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/api/embarcaciones")
public class ShowAllEmbarcacionesController {
    EmbarcacionRepository embarcacionRepository;
    private ModelAndView modelAndView = new ModelAndView();


    public ShowAllEmbarcacionesController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
        this.modelAndView.setViewName("embarcacion/showAllEmbarcacionesView");


        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping ("/showAllEmbarcaciones")
    ModelAndView showAllEmbarcaciones() {
        List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcaciones();

        if(embarcaciones.isEmpty()){
            this.modelAndView.addObject("listaEmbarcaciones", null);
        }
        else {
            this.modelAndView.addObject("listaEmbarcaciones", embarcaciones);
        }

        return modelAndView;
    }
}
