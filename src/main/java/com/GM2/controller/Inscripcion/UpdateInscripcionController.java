package com.GM2.controller.Inscripcion;

import com.GM2.model.domain.Inscripcion;
import com.GM2.model.repository.HijosRepository;
import com.GM2.model.repository.InscripcionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/inscripciones")
public class UpdateInscripcionController {

    InscripcionRepository inscripcionRepository;

    public UpdateInscripcionController(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.inscripcionRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping("/updateInscripcion")
    public ModelAndView updateInscripcionView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("updateInscripcionView");
        return modelAndView;
    }

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
