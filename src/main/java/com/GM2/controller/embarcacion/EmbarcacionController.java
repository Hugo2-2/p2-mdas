package com.GM2.controller.embarcacion;

import com.GM2.controller.patron.AssignPatronToEmbarcacion;
import com.GM2.model.domain.Embarcacion;
import com.GM2.model.repository.EmbarcacionRepository;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador web (MVC) para la gestión de Embarcaciones.
 * * Maneja las peticiones web para mostrar formularios y procesar la
 * creación de nuevas embarcaciones. También expone algunos
 * endpoints de API (híbridos) para obtener datos en JSON
 * que fueron principalmente usados para pruebas de
 * guardado de datos.
 * * @author gm2equipo1
 * @version 1.0
 */
@Controller
@RequestMapping("/api/embarcaciones")
public class EmbarcacionController {

    EmbarcacionRepository embarcacionRepository;
    EmbarcacionService embarcacionService;
    AssignPatronToEmbarcacion assignPatronToEmbarcacion;

    /**
     * Constructor para la inyección de dependencias.
     * Spring Boot inyectará automáticamente las instancias de los repositorios
     * y servicios necesarios.
     *
     * @param embarcacionRepository Repositorio para el acceso a datos de Embarcacion.
     * @param assignPatronToEmbarcacion Servicio para la lógica de asignación de patrones.
     * @param embarcacionService Servicio para la lógica de negocio de Embarcacion.
     */
    public EmbarcacionController(EmbarcacionRepository embarcacionRepository, AssignPatronToEmbarcacion assignPatronToEmbarcacion, EmbarcacionService embarcacionService) {
        this.embarcacionRepository = embarcacionRepository;
        this.embarcacionService = embarcacionService;
        this.assignPatronToEmbarcacion = assignPatronToEmbarcacion;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Endpoint API REST para obtener un listado de todas las embarcaciones.
     * Gracias a @ResponseBody, devuelve los datos en formato JSON.
     *
     * @return Una lista de objetos {@link Embarcacion}.
     */
    @GetMapping
    @ResponseBody
    public List<Embarcacion> getEmbarcaciones(){ return embarcacionRepository.findAllEmbarcaciones(); }

    /**
     * Endpoint API REST para obtener una embarcación específica por su matrícula.
     * Gracias a @ResponseBody, devuelve los datos en formato JSON.
     *
     * @param matricula La matrícula de la embarcación a buscar.
     * @return El objeto {@link Embarcacion} encontrado, o null si no existe.
     */
    @GetMapping("/{matricula}")
    @ResponseBody
    public Embarcacion getEmbarcacionByMatricula(@PathVariable String matricula){ return embarcacionRepository.findEmbarcacionByMatricula(matricula); }

    /**
     * Muestra el formulario web (vista HTML) para añadir una nueva embarcación.
     *
     * @return Un objeto {@link ModelAndView} que contiene el nombre de la vista
     * (addEmbarcacionView) y un objeto Embarcacion vacío para el formulario.
     */
    @GetMapping("/addEmbarcacion")
    public ModelAndView getAddEmbarcacionView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addEmbarcacionView");
        modelAndView.addObject("newEmbarcacion", new Embarcacion());
        return modelAndView;
    }

    /**
     * Procesa el envío del formulario (POST) para añadir una nueva embarcacion.
     * Utiliza el patrón Post-Redirect-Get para evitar envíos duplicados.
     *
     * @param newEmbarcacion     El objeto Embarcacion con los datos rellenados del formulario (@ModelAttribute).
     * @param sessionStatus      Controlador de estado de la sesión para limpiarla tras el envío.
     * @param redirectAttributes Interfaz para pasar mensajes (éxito/error) a la vista de redirección.
     * @return Un String que indica la URL a la que se debe redirigir.
     */
    @PostMapping("/addEmbarcacion")
    public String addEmbarcacion(@ModelAttribute Embarcacion newEmbarcacion,
                                 SessionStatus sessionStatus,
                                 RedirectAttributes redirectAttributes){

        System.out.println("[EmbarcacionController] Informacion recivida: matricula=" + newEmbarcacion.getMatricula() +
                " nombre=" + newEmbarcacion.getNombre() +
                " tipo=" + newEmbarcacion.getTipo() +
                " dimensiones=" + newEmbarcacion.getDimensiones() +
                " id_patron=" + newEmbarcacion.getIdPatron() +
                " plazas=" + newEmbarcacion.getPlazas());
        String resultado = embarcacionService.addEmbarcacion(newEmbarcacion);
        String nextPage;

        if(resultado.equals("EXITO")){
            redirectAttributes.addFlashAttribute("successMessage", "La embarcacion se ha ingresado correctamente");
        }
        else {
            redirectAttributes.addFlashAttribute("errorMessage", resultado);
        }
        nextPage = "redirect:/api/embarcaciones/addEmbarcacion";

        sessionStatus.setComplete();
        return nextPage;
    }
}
