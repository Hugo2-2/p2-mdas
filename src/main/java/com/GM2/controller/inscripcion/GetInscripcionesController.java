package com.GM2.controller.inscripcion;

import com.GM2.model.domain.Inscripcion;
import com.GM2.model.repository.InscripcionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Controlador web (MVC) para la visualización de Inscripciones.
 * Maneja las peticiones web para mostrar listados de inscripciones
 * registradas en el club náutico. Proporciona endpoints para
 * consultar y visualizar la información de todas las inscripciones,
 * incluyendo datos de socios titulares, cuotas e hijos asociados.
 *
 * @author gm2equipo1
 * @version 1.0
 */
@Controller
@RequestMapping("/api/inscripciones")
public class GetInscripcionesController {

    InscripcionRepository inscripcionRepository;

    /**
     * Constructor para la inyección de dependencias.
     * Spring Boot inyectará automáticamente las instancias de los repositorios
     * y servicios necesarios.
     *
     * @param inscripcionRepository Repositorio para el acceso a datos de Inscripcion.
     */
    public GetInscripcionesController(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.inscripcionRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Muestra la lista completa de inscripciones registradas en el club náutico.
     * Recupera todas las inscripciones de la base de datos, incluyendo
     * automáticamente los hijos asociados cuando la cuota supera los 300€,
     * y las envía a la vista para su visualización en formato de lista.
     *
     * @return Un objeto {@link ModelAndView} que contiene el nombre de la vista
     *         (listInscripciones) y la lista de inscripciones para mostrar.
     */
    @GetMapping("/")
    public ModelAndView getInscripciones(){
        ModelAndView mv = new ModelAndView("inscripcion/listInscripcionesView");

        List<Inscripcion> inscripciones = inscripcionRepository.findAllInscripciones();

        mv.addObject("inscripciones", inscripciones);

        return mv;
    }
}
