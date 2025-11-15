package com.GM2.controller.patron;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Patron;
import com.GM2.model.repository.PatronRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/api/patrones")
public class ShowAllPatronesController {

    PatronRepository patronRepository;
    private ModelAndView modelAndView = new ModelAndView();


    public ShowAllPatronesController(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
        this.modelAndView.setViewName("patron/showAllPatronesView");

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }


    @GetMapping("/showAllPatrones")
    ModelAndView showAllEmbarcaciones() {
        List<Patron> embarcaciones = patronRepository.findAllPatrones();

        if(embarcaciones.isEmpty()){
            this.modelAndView.addObject("listaPatrones", null);
        }
        else {
            this.modelAndView.addObject("listaPatrones", embarcaciones);
        }

        return modelAndView;
    }

}
