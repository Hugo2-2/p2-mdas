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
 * Controlador encargado de gestionar la consulta de embarcaciones por tipo.
 * Permite mostrar un formulario donde el usuario puede seleccionar un tipo
 * de embarcación y visualizar todas las que coinciden con dicho criterio.
 *
 * Este controlador implementa el requisito funcional B.4 del sistema
 * (Consultar embarcación por tipo).
 *
 * La vista asociada es "embarcacion/consultarEmbarcacionesByTipoView".
 *
 * @author gm2equipo1
 * @version 1.0
 */
@Controller
@RequestMapping("/api/embarcaciones")
public class ShowEmbarcacionesByTypeController {

    private final EmbarcacionRepository embarcacionRepository;
    PatronRepository patronRepository;
    private ModelAndView modelAndView = new ModelAndView();

    /**
     * Constructor encargado de inyectar las dependencias necesarias y de configurar
     * los repositorios indicando el archivo donde se encuentran las consultas SQL.
     *
     * @param embarcacionRepository Repositorio para la gestión de datos de embarcaciones.
     * @param patronRepository Repositorio para la gestión de datos de patrones.
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
     * Muestra el formulario de consulta por tipo y, si se proporciona un tipo,
     * procesa la búsqueda correspondiente.
     *
     * El parámetro es opcional: si no se recibe un tipo, simplemente se mostrará
     * el formulario vacío sin resultados.
     *
     * @param tipo Tipo de embarcación a buscar. Si no se especifica, se utiliza "none"
     *             indicando que no debe ejecutarse ninguna búsqueda.
     * @return ModelAndView configurado con la vista y, si corresponde, los resultados.
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