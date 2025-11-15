package com.GM2.controller.Socio;

import com.GM2.model.domain.Socio;
import com.GM2.model.repository.SocioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Controlador web (MVC) para la visualización de Socios.
 * Maneja las peticiones web para mostrar listados de socios
 * registrados en el club náutico. Proporciona endpoints para
 * consultar y visualizar la información de todos los socios.
 *
 * @author gm2equipo1
 * @version 1.0
 */
@Controller
@RequestMapping("/api/socios")
public class ShowSociosController {
    SocioRepository socioRepository;

    /**
     * Constructor para la inyección de dependencias.
     * Spring Boot inyectará automáticamente las instancias de los repositorios
     * y servicios necesarios.
     *
     * @param socioRepository Repositorio para el acceso a datos de Socio.
     */
    public ShowSociosController(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Muestra la lista completa de socios registrados en el club náutico.
     * Recupera todos los socios de la base de datos y los envía a la vista
     * para su visualización en formato de lista.
     *
     * @return Un objeto {@link ModelAndView} que contiene el nombre de la vista
     *         (listSocios) y la lista de socios para mostrar.
     */
    @GetMapping("/")
    public ModelAndView getSocios() {
        List<Socio> socios = socioRepository.findAllSocios();

        ModelAndView mv = new ModelAndView("listSocios");

        mv.addObject("socios", socios);

        return mv;
    }
}
