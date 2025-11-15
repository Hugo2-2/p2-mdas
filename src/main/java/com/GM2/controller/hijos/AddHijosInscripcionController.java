package com.GM2.controller.hijos;

import com.GM2.model.repository.InscripcionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/inscripciones")
public class AddHijosInscripcionController {

    private final InscripcionRepository inscripcionRepository;

    public AddHijosInscripcionController(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.inscripcionRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

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

    @PostMapping("/addHijosView")
    public String addHijos(
            @RequestParam("dniTitular") String dniTitular,
            @RequestParam("hijo_dni") List<String> dnisHijos,
            @RequestParam("nombre") List<String> nombreHijos,
            @RequestParam("apellidos") List<String> apellidosHijos,
            @RequestParam("fechaNacimiento") List<LocalDate> fechaNacimientoHijos,
            RedirectAttributes redirectAttributes) {

        String resultado = inscripcionRepository.updateInscripcioConHijos(dniTitular, dnisHijos, nombreHijos, apellidosHijos, fechaNacimientoHijos);

        if (resultado.equals("EXITO")) {
            redirectAttributes.addFlashAttribute("mensajeExito", "Inscripción (con hijos) guardada.");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", resultado);
        }
        return "redirect:/api/inscripciones/updateInscripcion";
    }
}
