package com.GM2.controller.patron;

import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.PatronRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/embarcaciones")
public class AssignPatronToEmbarcacionController {
    EmbarcacionRepository embarcacionRepository;
    PatronRepository patronRepository;

    public AssignPatronToEmbarcacionController(EmbarcacionRepository embarcacionRepository, PatronRepository patronRepository) {
        this.embarcacionRepository = embarcacionRepository;
        this.patronRepository = patronRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping("/asociarPatron")
    public ModelAndView getAsociatePatronView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("asociatePatronToEmbarcacionView");
        modelAndView.addObject("todasLasEmbarcaciones", embarcacionRepository.findAllEmbarcaciones());
        modelAndView.addObject("patronesLibres", patronRepository.findAllFreePatrones());
        return modelAndView;
    }

    @PostMapping("/asociarPatron")
    public String asociatePatron(@RequestParam String matricula,
                                 @RequestParam String dniPatronNuevo,
                                 SessionStatus sessionStatus,
                                 RedirectAttributes redirectAttributes, //Esto es para poder lanzar mensajes de error sin tener que crear vistas nuevas
                                 Model model) { // 'Model' es para pasar datos a la vista de confirmación

        // 1. Comprobar si el nuevo patrón está libre
//        if (embarcacionRepository.isPatronAssignedToEmbarcacion(dniPatronNuevo)) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Error: El patrón seleccionado ya está asignado a otra barca.");
//            return "redirect:/api/embarcaciones/asociarPatron"; // Vuelve al formulario
//        }

        // 2. Comprobar si la EMBARCACIÓN tiene un patrón antiguo
        String dniPatronAntiguo = embarcacionRepository.getPatronAssignedToEmbarcacion(matricula);

        if (dniPatronAntiguo != null) {

            // Si tiene patron antiguo. Hay que confirmar.
            // Preparamos la vista de confirmación sin actualizar la base de datos

            // Pasamos los datos a la vista 'patronAssignedView'
            model.addAttribute("matricula", matricula);
            model.addAttribute("dniPatronNuevo", dniPatronNuevo);
            model.addAttribute("dniPatronAntiguo", dniPatronAntiguo);

            return "patronAssignedView"; // Muestra la vista de confirmación

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
