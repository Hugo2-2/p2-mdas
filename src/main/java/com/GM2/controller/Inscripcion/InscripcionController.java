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
public class InscripcionController {

    private final HijosRepository hijosRepository;
    InscripcionRepository inscripcionRepository;

    public InscripcionController(InscripcionRepository inscripcionRepository, HijosRepository hijosRepository) {
        this.hijosRepository = hijosRepository;
        this.inscripcionRepository = inscripcionRepository;
    }

    @GetMapping("/")
    public ModelAndView getInscripciones(){
        ModelAndView mv = new ModelAndView("listInscripciones");

        List<Inscripcion> inscripciones = inscripcionRepository.findAllInscripciones();

        mv.addObject("inscripciones", inscripciones);

        return mv;
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
