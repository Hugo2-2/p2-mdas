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

/**
 * Controlador web (MVC) para la funcionalidad de consultar embarcaciones.
 * * Maneja la lógica para mostrar la vista "consultarEmbarcacionesView"
 * y procesar la búsqueda de embarcaciones por tipo.
 * * @author gm2equipo1
 * @version 1.0
 */
@Controller
@RequestMapping("/api/embarcaciones")
public class ShowEmbarcacionesByType {

    private final EmbarcacionService embarcacionService;
    PatronRepository patronRepository;

    private ModelAndView modelAndView = new ModelAndView();

    /**
     * Constructor para la inyección de dependencias del servicio de embarcaciones.
     *
     * @param embarcacionService El servicio que contiene la lógica de negocio.
     */
    public ShowEmbarcacionesByType(PatronRepository patronRepository, EmbarcacionService embarcacionService) {
        this.patronRepository = patronRepository;
        this.modelAndView.setViewName("consultarEmbarcacionesView");
        this.embarcacionService = embarcacionService;
    }

    /**
     * Muestra la página para consultar embarcaciones y procesa la búsqueda por tipo.
     * Cumple con el requisito B.4 (Consultar embarcación por tipo)
     *
     * @param tipo El tipo de embarcación a buscar (ej. "VELERO").
     * Es opcional; si no se provee, se usa "none".
     * @return Un objeto {@link ModelAndView} con la vista y los datos del modelo.
     */
    @GetMapping("/consultarEmbarcacionesPorTipo")
    public ModelAndView showEmbarcacionesByType(
            @RequestParam(value = "tipo", defaultValue = "none") String tipo) {

        //Comprobamos si el parámetro se ha proporcionado
        if (!tipo.equals("none")) {
            // Si SÍ se proporcionó, ejecutamos la búsqueda
            List<Embarcacion> embarcaciones = embarcacionService.findAllEmbarcacionesByTipo(tipo);

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