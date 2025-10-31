package com.GM2.controller.embarcacion;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.PatronRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/api/embarcaciones")
public class ShowEmbarcacionesByType {

    EmbarcacionRepository embarcacionRepository;
    PatronRepository patronRepository;

    private ModelAndView modelAndView = new ModelAndView();

    public ShowEmbarcacionesByType(EmbarcacionRepository embarcacionRepository, PatronRepository patronRepository) {
        this.embarcacionRepository = embarcacionRepository;
        this.patronRepository = patronRepository;
        this.modelAndView.setViewName("consultarEmbarcacionesView");
    }

    @GetMapping("/consultarEmbarcacionesPorTipo")
    public ModelAndView showEmbarcacionesByType(
            @RequestParam(value = "tipo", defaultValue = "none") String tipo) {

        //Comprobamos si el parámetro se ha proporcionado
        if (!tipo.equals("none")) {
            // Si SÍ se proporcionó, ejecutamos la búsqueda
            List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcacionesByTipo(tipo);

            // Y añadimos los resultados al modelo
            this.modelAndView.addObject("listaEmbarcaciones", embarcaciones);
            this.modelAndView.addObject("tipoBuscado", tipo);
        } else {
            // Si no se proporcionó , limpiamos los resultados
            this.modelAndView.addObject("listaEmbarcaciones", null);
            this.modelAndView.addObject("tipoBuscado", null);
        }

        return this.modelAndView;
    }
}