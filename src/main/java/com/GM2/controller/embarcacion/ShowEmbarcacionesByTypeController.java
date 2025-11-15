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
public class ShowEmbarcacionesByTypeController {

    private final EmbarcacionRepository embarcacionRepository;
    PatronRepository patronRepository;

    private ModelAndView modelAndView = new ModelAndView();

    /**
     * Constructor para la inyección de dependencias de los repositorios.
     *
     * Siguiendo la arquitectura MVC estricta del proyecto, este constructor
     * recibe las instancias de los repositorios (el Modelo) y es
     * responsable de configurarlas, como indicar la ruta al archivo
     * de consultas SQL.
     *
     * @param embarcacionRepository El repositorio para acceder a los datos de Embarcacion.
     * @param patronRepository El repositorio para acceder a los datos de Patron (necesario
     * para las validaciones de EmbarcacionRepository).
     */
    public ShowEmbarcacionesByTypeController(EmbarcacionRepository embarcacionRepository, PatronRepository patronRepository) {
        this.embarcacionRepository = embarcacionRepository;
        this.patronRepository = patronRepository;
        this.modelAndView.setViewName("embarcacion/consultarEmbarcacionesByTipoView");

        // Configuración del archivo de propiedades SQL desde el Controlador
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
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