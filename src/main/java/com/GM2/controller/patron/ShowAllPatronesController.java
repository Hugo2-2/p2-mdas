package com.GM2.controller.patron;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.domain.Patron;
import com.GM2.model.repository.PatronRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Controlador web (MVC) encargado de mostrar todos los patrones registrados en el sistema.
 *
 * Gestiona la visualización de la vista "showAllPatronesView", proporcionando al modelo
 * la lista completa de patrones. Si no existen patrones registrados, se envía un valor nulo
 * al modelo para indicar que la lista está vacía.
 */
@Controller
@RequestMapping("/api/patrones")
public class ShowAllPatronesController {

    PatronRepository patronRepository;
    private ModelAndView modelAndView = new ModelAndView();

    // Clean Code - Regla 9: Se ha eliminado la cabecera de esta función por ser un método simple y autoexplicativo.
    public ShowAllPatronesController(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
        this.modelAndView.setViewName("patron/showAllPatronesView");

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping("/showAllPatrones")
    ModelAndView getAll() {
        List<Patron> patrones = patronRepository.findAllPatrones();

        if(patrones.isEmpty()){
            this.modelAndView.addObject("listaPatrones", null);
        }
        else {
            this.modelAndView.addObject("listaPatrones", patrones);
        }

        return modelAndView;
    }

}
