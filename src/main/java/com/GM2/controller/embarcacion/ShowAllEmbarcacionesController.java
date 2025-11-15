package com.GM2.controller.embarcacion;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.repository.EmbarcacionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Controlador encargado de mostrar todas las embarcaciones registradas en el sistema.
 * Accede al repositorio correspondiente, obtiene la lista completa de embarcaciones
 * y la envía a la vista para su visualización.
 *
 * La vista asociada es "embarcacion/showAllEmbarcacionesView", donde se mostrará
 * una tabla con las embarcaciones encontradas o un mensaje indicando que no existen.
 */
@Controller
@RequestMapping("/api/embarcaciones")
public class ShowAllEmbarcacionesController {
    EmbarcacionRepository embarcacionRepository;
    private ModelAndView modelAndView = new ModelAndView();

    /**
     * Constructor para la inyección de dependencias. Inicializa la vista que se utilizará
     * y establece el archivo de propiedades SQL que empleará el repositorio.
     *
     * @param embarcacionRepository Repositorio encargado de gestionar el acceso a datos de embarcaciones.
     */
    public ShowAllEmbarcacionesController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
        this.modelAndView.setViewName("embarcacion/showAllEmbarcacionesView");


        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Maneja la solicitud GET para obtener todas las embarcaciones disponibles en el sistema.
     * Recupera la lista desde el repositorio y la envía a la vista para su representación.
     *
     * Si la lista está vacía, se envía un valor nulo para permitir mostrar un mensaje
     * indicándolo en la interfaz.
     *
     * @return ModelAndView con la lista de embarcaciones o nulo si no existen registros.
     */
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
