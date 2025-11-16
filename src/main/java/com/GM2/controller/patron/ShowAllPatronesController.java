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

    /**
     * Constructor para la inyección de dependencias del repositorio de patrones.
     *
     * Configura además la ruta del archivo de propiedades SQL utilizado por el repositorio
     * para las consultas a la base de datos.
     *
     * @param patronRepository Repositorio para acceder a los datos de patrones.
     */
    public ShowAllPatronesController(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
        this.modelAndView.setViewName("patron/showAllPatronesView");

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Muestra la vista con la lista de todos los patrones registrados.
     *
     * Obtiene todos los patrones mediante el repositorio. Si la lista está vacía,
     * se pasa un valor nulo al modelo; de lo contrario, se pasa la lista completa.
     *
     * @return ModelAndView con la vista "showAllPatronesView" y los datos de los patrones.
     */
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
