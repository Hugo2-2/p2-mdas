package com.GM2.controller.Inscripcion;


import com.GM2.model.repository.InscripcionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador web (MVC) para la actualización de Inscripciones.
 * Maneja las peticiones web para mostrar formularios y procesar la
 * actualización de inscripciones existentes. Gestiona el flujo de trabajo
 * para añadir segundos adultos y hijos a las inscripciones familiares,
 * incluyendo redirecciones condicionales según las opciones seleccionadas.
 *
 * @author gm2equipo1
 * @version 1.0
 */
@Controller
@RequestMapping("/api/inscripciones")
public class UpdateInscripcionController {

    InscripcionRepository inscripcionRepository;

    /**
     * Constructor para la inyección de dependencias.
     * Spring Boot inyectará automáticamente las instancias de los repositorios
     * y servicios necesarios.
     *
     * @param inscripcionRepository Repositorio para el acceso a datos de Inscripcion.
     */
    public UpdateInscripcionController(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.inscripcionRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Muestra el formulario web (vista HTML) para actualizar una inscripción existente.
     * Permite añadir segundos adultos e hijos a las inscripciones familiares.
     *
     * @return Un objeto {@link ModelAndView} que contiene el nombre de la vista
     *         (updateInscripcionView) para el formulario de actualización.
     */
    @GetMapping("/updateInscripcion")
    public ModelAndView updateInscripcionView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("updateInscripcionView");
        return modelAndView;
    }

    /**
     * Procesa el envío del formulario (POST) para actualizar una inscripción.
     * Utiliza el patrón Post-Redirect-Get para evitar envíos duplicados.
     * Maneja dos flujos de trabajo: actualización simple (solo segundo adulto)
     * o redirección al formulario de hijos si se desea continuar añadiendo menores.
     *
     * @param dniTitular DNI del socio titular de la inscripción a actualizar.
     * @param dniSegundoAdulto DNI del segundo adulto a añadir (opcional).
     * @param anadirHijos Parámetro que indica si se desean añadir hijos (opcional).
     * @param numeroHijos Número de hijos a añadir (opcional).
     * @param siguientePaso Indica el flujo de trabajo: "continuarHijos" o finalización.
     * @param redirectAttributes Interfaz para pasar mensajes (éxito/error) a la vista de redirección.
     * @return Un String que indica la URL a la que se debe redirigir.
     */
    @PostMapping("/updateInscripcion")
    public String updateInscripcion(
            @RequestParam(name = "dniTitular") String dniTitular,
            @RequestParam(name = "dniSegundoAdulto", required = false) String dniSegundoAdulto,
            @RequestParam(name = "anadirHijos", required = false) String anadirHijos,
            @RequestParam(name = "numeroHijos", required = false) Integer numeroHijos, // Integer es mejor para números
            @RequestParam(name = "siguientePaso") String siguientePaso, // Recibimos el 'name="siguientePaso"' del botón pulsado
            RedirectAttributes redirectAttributes) {

        System.out.println("[InscripcionController] Informacion recivida: dniTitular=" + dniTitular +
                " dniSegundoAdulto=" + dniSegundoAdulto +
                " anadirHijos=" + anadirHijos +
                " numeroHijos=" + numeroHijos +
                " siguientePaso=" + siguientePaso);

        if (siguientePaso.equals("continuarHijos")) {

            // Si el usuario quiere añadir hijos
            redirectAttributes.addFlashAttribute("dniTitular", dniTitular);
            redirectAttributes.addFlashAttribute("dniSegundoAdulto", dniSegundoAdulto);
            redirectAttributes.addFlashAttribute("numeroHijos", numeroHijos);

            //Guardamos el segundo adulto
            String resultado = inscripcionRepository.updateInscripcioSinHijos(dniTitular, dniSegundoAdulto);
            if (resultado.equals("EXITO")) {
                redirectAttributes.addFlashAttribute("mensajeExito", "Inscripción (sin hijos) guardada.");
            } else {
                redirectAttributes.addFlashAttribute("mensajeError", resultado);
            }

            // Redirige a la vista de "Añadir Hijos"
            return "redirect:/api/inscripciones/addHijosView";

        } else {

            String resultado = inscripcionRepository.updateInscripcioSinHijos(dniTitular, dniSegundoAdulto);

            if (resultado.equals("EXITO")) {
                redirectAttributes.addFlashAttribute("mensajeExito", "Inscripción (sin hijos) guardada.");
            } else {
                redirectAttributes.addFlashAttribute("mensajeError", resultado);
            }
            return "redirect:/api/inscripciones/updateInscripcion";
        }
    }
}
