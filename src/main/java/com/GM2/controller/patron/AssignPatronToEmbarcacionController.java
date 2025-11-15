package com.GM2.controller.patron;

import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.PatronRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador web (MVC) encargado de gestionar la asignación de patrones a embarcaciones.
 *
 * Este controlador muestra la vista donde se pueden seleccionar embarcaciones y patrones
 * libres y procesa la asignación de un patrón a una embarcación. Si la embarcación ya tiene
 * un patrón asignado, se solicita confirmación antes de realizar el reemplazo.
 *
 * Gestiona tanto la visualización de los formularios como la lógica de asignación y actualización
 * del modelo en la base de datos a través de los repositorios correspondientes.
 */
@Controller
@RequestMapping("/api/embarcaciones")
public class AssignPatronToEmbarcacionController {
    EmbarcacionRepository embarcacionRepository;
    PatronRepository patronRepository;

    /**
     * Constructor para la inyección de dependencias de los repositorios.
     *
     * Configura además la ruta del archivo de propiedades SQL que ambos repositorios utilizarán
     * para sus operaciones de acceso a datos.
     *
     * @param embarcacionRepository Repositorio para acceder a los datos de embarcaciones.
     * @param patronRepository Repositorio para acceder a los datos de patrones.
     */
    public AssignPatronToEmbarcacionController(EmbarcacionRepository embarcacionRepository, PatronRepository patronRepository) {
        this.embarcacionRepository = embarcacionRepository;
        this.patronRepository = patronRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Muestra la vista para asignar un patrón a una embarcación.
     *
     * Se envían al modelo:
     * - Todas las embarcaciones existentes.
     * - Todos los patrones libres disponibles.
     *
     * @return ModelAndView con la vista de asignación de patrón y los datos necesarios.
     */
    @GetMapping("/asociarPatron")
    public ModelAndView getAsociatePatronView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("patron/asociatePatronToEmbarcacionView");
        modelAndView.addObject("todasLasEmbarcaciones", embarcacionRepository.findAllEmbarcaciones());
        modelAndView.addObject("patronesLibres", patronRepository.findAllFreePatrones());
        return modelAndView;
    }

    /**
     * Procesa la asignación de un patrón a una embarcación.
     *
     * Flujo:
     * 1. Comprueba si la embarcación ya tiene un patrón asignado.
     * 2. Si existe un patrón antiguo, se solicita confirmación mostrando la vista de confirmación.
     * 3. Si la embarcación está libre, se asigna directamente el patrón.
     * 4. Se envían mensajes flash para informar del éxito o error de la operación.
     *
     * @param matricula Matrícula de la embarcación a la que se desea asignar el patrón.
     * @param dniPatronNuevo DNI del patrón que se desea asignar.
     * @param sessionStatus Estado de la sesión para indicar la finalización del flujo.
     * @param redirectAttributes Atributos flash para enviar mensajes a la vista tras redirección.
     * @param model Modelo para enviar datos a la vista de confirmación si se requiere reemplazo de patrón.
     * @return Redirección al formulario principal o vista de confirmación de reemplazo.
     */
    @PostMapping("/asociarPatron")
    public String asociatePatron(@RequestParam String matricula,
                                 @RequestParam String dniPatronNuevo,
                                 SessionStatus sessionStatus,
                                 RedirectAttributes redirectAttributes, //Esto es para poder lanzar mensajes de error sin tener que crear vistas nuevas
                                 Model model) { // 'Model' es para pasar datos a la vista de confirmación

        // Comprobar si la EMBARCACIÓN tiene un patrón antiguo
        String dniPatronAntiguo = embarcacionRepository.getPatronAssignedToEmbarcacion(matricula);

        if (dniPatronAntiguo != null) {

            // Si tiene patron antiguo. Hay que confirmar.
            // Preparamos la vista de confirmación sin actualizar la base de datos

            // Pasamos los datos a la vista 'patronAssignedView'
            model.addAttribute("matricula", matricula);
            model.addAttribute("dniPatronNuevo", dniPatronNuevo);
            model.addAttribute("dniPatronAntiguo", dniPatronAntiguo);

            return "patron/patronAssignedView"; // Muestra la vista de confirmación

        } else {

            // No tiene patron, está libre. Está libre. Asignamos directamente.
            boolean success = embarcacionRepository.updatePatron(dniPatronNuevo, matricula);

            if(success) {
                redirectAttributes.addFlashAttribute("successMessage", "Patrón asignado correctamente a la embarcación libre.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error al asignar el patrón.");
            }

            sessionStatus.setComplete(); // Solo limpiamos si el flujo termina
            return "redirect:/api/embarcaciones/asociarPatron"; // Vuelve al formulario
        }
    }
}
