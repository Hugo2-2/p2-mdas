package com.GM2.controller.hijos;

import com.GM2.model.repository.InscripcionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;


/**
 * Controlador encargado de gestionar la funcionalidad de añadir hijos
 * a una inscripción existente.
 *
 * Esta funcionalidad forma parte del proceso de actualización de una inscripción
 * y permite registrar uno o varios hijos asociados a un socio titular.
 *
 * El controlador muestra un formulario dinámico basado en el número de hijos
 * y posteriormente procesa la inserción de esos datos en la base de datos.
 *
 * Vista principal: "hijos/addHijosView".
 *
 * @author gm2
 * @version 1.0
 */
@Controller
@RequestMapping("/api/inscripciones")
public class AddHijosInscripcionController {

    private final InscripcionRepository inscripcionRepository;

    /**
     * Constructor encargado de inyectar el repositorio de inscripciones y de configurar
     * la ruta al archivo de consultas SQL.
     *
     * @param inscripcionRepository Repositorio para operaciones relacionadas con inscripciones.
     */
    public AddHijosInscripcionController(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.inscripcionRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Muestra la vista para introducir los datos de los hijos.
     * Esta vista se accede desde el flujo de actualización de inscripción.
     *
     * Los parámetros "dniTitular" y "numeroHijos" se reciben desde un redirect previo
     * mediante {@link RedirectAttributes}.
     *
     * @param dniTitular DNI del socio titular.
     * @param numeroHijos Número total de hijos que se desean registrar.
     * @param redirectAttributes Objeto para pasar mensajes de error si los datos no son válidos.
     * @return ModelAndView configurado con la vista o una redirección en caso de error.
     */
    @GetMapping("/addHijosView")
    public ModelAndView addHijosView(
            // Recibimos los datos enviados por 'RedirectAttributes'
            @ModelAttribute("dniTitular") String dniTitular,
            @ModelAttribute("numeroHijos") Integer numeroHijos,
            RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView = new ModelAndView();

        if (numeroHijos == null || numeroHijos <= 0) {
            redirectAttributes.addFlashAttribute("mensajeError", "Número de hijos no válido.");
            modelAndView.setViewName("redirect:/api/inscripciones/updateInscripcion");
            return modelAndView;
        }

        modelAndView.setViewName("hijos/addHijosView");

        // Pasamos los datos a la vista
        modelAndView.addObject("dniTitular", dniTitular);
        modelAndView.addObject("numeroHijos", numeroHijos);

        return modelAndView;
    }

    /**
     * Procesa el formulario de introducción de hijos y actualiza
     * la inscripción en la base de datos.
     *
     * Este método recibe listas paralelas con los datos de cada hijo:
     * DNI, nombre, apellidos y fecha de nacimiento.
     *
     * @param dniTitular DNI del socio titular.
     * @param dnisHijos Lista de DNIs de los hijos.
     * @param nombreHijos Lista de nombres.
     * @param apellidosHijos Lista de apellidos.
     * @param fechaNacimientoHijos Lista de fechas de nacimiento.
     * @param redirectAttributes Permite enviar mensajes de éxito o error.
     * @return Redirección al formulario de actualización de inscripción.
     */
    @PostMapping("/addHijosView")
    public String addHijos(
            @RequestParam("dniTitular") String dniTitular,
            @RequestParam("hijo_dni") List<String> dnisHijos,
            @RequestParam("nombre") List<String> nombreHijos,
            @RequestParam("apellidos") List<String> apellidosHijos,
            @RequestParam("fechaNacimiento") List<LocalDate> fechaNacimientoHijos,
            RedirectAttributes redirectAttributes) {

        String resultado = inscripcionRepository.updateInscripcionConHijos(dniTitular, dnisHijos, nombreHijos, apellidosHijos, fechaNacimientoHijos);

        if (resultado.equals("EXITO")) {
            redirectAttributes.addFlashAttribute("mensajeExito", "Inscripción (con hijos) guardada.");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", resultado);
        }
        return "redirect:/api/inscripciones/updateInscripcion";
    }
}
