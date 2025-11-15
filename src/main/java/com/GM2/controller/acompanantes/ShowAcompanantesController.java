package com.GM2.controller.acompanantes;

import com.GM2.model.domain.Acompanante;
import com.GM2.model.repository.AcompananteRepository;
import com.GM2.model.repository.AlquilerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/acompanantes")
public class ShowAcompanantesController {

    AcompananteRepository acompanantesRepository;
    AlquilerRepository alquilerRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param acompanantesRepository Repositorio de acompañantes para operaciones de datos
     * @param alquilerRepository Repositorio de alquileres para operaciones de datos
     */
    public ShowAcompanantesController(AcompananteRepository acompanantesRepository, AlquilerRepository alquilerRepository) {
        this.acompanantesRepository = acompanantesRepository;
        this.alquilerRepository = alquilerRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.acompanantesRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.alquilerRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Muestra el formulario para gestionar acompañantes de un alquiler.
     *
     * @param alquilerId ID del alquiler
     * @param plazas Número de plazas disponibles
     * @return ModelAndView con el formulario de acompañantes
     */
    @GetMapping("/{alquilerId}/{plazas}")
    public ModelAndView mostrarFormularioAcompanantes(@PathVariable Integer alquilerId, @PathVariable int plazas) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addAcompanante");

        // 1. Cargar acompañantes existentes de la base de datos
        List<Acompanante> acompanantesExistentes = acompanantesRepository.findAcompananteByAlquiler(alquilerId);

        // Si es null, crear lista vacía
        if (acompanantesExistentes == null) {
            acompanantesExistentes = new ArrayList<>();
        }

        // 2. Calcular cuántos acompañantes nuevos se pueden añadir
        int plazasDisponibles = plazas - 1 - acompanantesExistentes.size();

        // 3. Crear lista para nuevos acompañantes (solo los que caben)
        List<Acompanante> nuevosAcompanantes = new ArrayList<>();
        for (int i = 0; i < plazasDisponibles; i++) {
            nuevosAcompanantes.add(new Acompanante());
        }

        // 4. Pasar todos los datos al modelo
        modelAndView.addObject("alquilerId", alquilerId);
        modelAndView.addObject("plazas", plazas);
        modelAndView.addObject("acompanantesExistentes", acompanantesExistentes);
        modelAndView.addObject("acompanantes", nuevosAcompanantes); // Los nuevos que se pueden añadir
        modelAndView.addObject("plazasDisponibles", plazasDisponibles);

        return modelAndView;
    }
}
