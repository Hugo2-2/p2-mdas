package com.GM2.controller.Inscripcion;

import ch.qos.logback.core.model.Model;
import com.GM2.model.domain.Acompanante;
import com.GM2.model.domain.Hijos;
import com.GM2.model.domain.Inscripcion;
import com.GM2.model.repository.HijosRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    private final HijosRepository hijosRepository;
    InscripcionService inscripcionService;

    public InscripcionController(InscripcionService inscripcionService, HijosRepository hijosRepository) {  this.inscripcionService = inscripcionService;
        this.hijosRepository = hijosRepository;
        this.inscripcionService = inscripcionService;
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
            String resultado = inscripcionService.updateInscripcioSinHijos(dniTitular, dniSegundoAdulto);
            if (resultado.equals("EXITO")) {
                redirectAttributes.addFlashAttribute("mensajeExito", "Inscripción (sin hijos) guardada.");
            } else {
                redirectAttributes.addFlashAttribute("mensajeError", resultado);
            }

            // Redirige a la vista de "Añadir Hijos"
            return "redirect:/api/inscripciones/addHijosView";

        } else {

            String resultado = inscripcionService.updateInscripcioSinHijos(dniTitular, dniSegundoAdulto);

            if (resultado.equals("EXITO")) {
                redirectAttributes.addFlashAttribute("mensajeExito", "Inscripción (sin hijos) guardada.");
            } else {
                redirectAttributes.addFlashAttribute("mensajeError", resultado);
            }
            return "redirect:/api/inscripciones/updateInscripcion";
        }
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

        modelAndView.setViewName("addHijosView");

        // Pasamos los datos a la vista
        modelAndView.addObject("dniTitular", dniTitular);
        modelAndView.addObject("numeroHijos", numeroHijos);

        return modelAndView;
    }

    @PostMapping("/addHijosView")
    public String addHijos(
            @RequestParam("dniTitular") String dniTitular,
            @RequestParam("hijo_dni") List<String> dnisHijos,
            RedirectAttributes redirectAttributes) {

        String resultado = inscripcionService.updateInscripcioConHijos(dniTitular, dnisHijos);

        if (resultado.equals("EXITO")) {
            redirectAttributes.addFlashAttribute("mensajeExito", "Inscripción (con hijos) guardada.");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", resultado);
        }
        return "redirect:/api/inscripciones/updateInscripcion";
    }

    @PostMapping("/upgradeInscripcion")
    public String upgradeInscripcion(@ModelAttribute("inscripcion") Inscripcion inscripcion, RedirectAttributes redirectAttributes) {
        String resultado = inscripcionService.updateInscripcion(inscripcion);

        if (resultado.equals("EXITO")) {
            redirectAttributes.addFlashAttribute("mensaje", "Inscripción guardada exitosamente");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", resultado);
        }

        return "redirect:/api/inscripciones/upgradeInscripcion?id=" + String.valueOf(inscripcion.getId()) ;
    }
}
