package com.GM2.controller.Socio;

import com.GM2.model.domain.Socio;
import com.GM2.model.repository.SocioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("/api/socios")
public class SocioController {

    SocioRepository socioRepository;

    public SocioController(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }
    @GetMapping("/")
    public ModelAndView getSocios() {
        List<Socio> socios = socioRepository.findAllSocios();

        ModelAndView mv = new ModelAndView("listSocios");

        mv.addObject("socios", socios);

        return mv;
    }

    @GetMapping("/addSocio")
    public ModelAndView mostrarFormularioSocio() {
        ModelAndView mv = new ModelAndView("addSocioView");
        mv.addObject("socio", new Socio());
        return mv;
    }

    @PostMapping("/addSocio")
    public String addSocio(@ModelAttribute Socio socio,
                           @RequestParam(name = "ampliarInscripcion", required = false) String ampliarInscripcion,
                           RedirectAttributes redirectAttributes) {

        socio.setFechaInscripcion(LocalDate.now());

        //Mensajes para depurar en terminal
        System.out.println("[SocioController] Informacion recivida: nombre=" + socio.getNombre() +
                " apellidos=" + socio.getApellidos() +
                " dni=" + socio.getDni() +
                " fechaNacimiento=" + socio.getFechaNacimiento() +
                " direccion=" + socio.getDireccion() +
                " fechaInscripcion=" + socio.getFechaInscripcion() +
                " esTitular=" + socio.getEsTitular() +
                " tieneLicenciaPatron=" + socio.getTieneLicenciaPatron());

        //Usaremos estas funciones para añadir al socio y mostrar mensajes de error
        String mensaje = socioRepository.addSocio(socio);

        //Evaluamos el mensaje que se mostrará en las flashcards de error
        if(mensaje.equals("EXITO")) {
            redirectAttributes.addFlashAttribute("mensaje", "Socio guardado exitosamente");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", mensaje);
        }

        //Confirmamos que la checkbox de la vista está marcada
        boolean quiereAmpliar = (ampliarInscripcion != null && ampliarInscripcion.equals("true"));

        if(socio.getEsTitular() && quiereAmpliar) {
            // Pasamos el DNI del socio recién creado a la siguiente página
            redirectAttributes.addFlashAttribute("dniTitular", socio.getDni());

            // Redirigimos al nuevo formulario de inscripción familiar pasando el id de la inscripción como parámetro en el enlace
            return "redirect:/api/inscripciones/updateInscripcion";
        } else {
            redirectAttributes.addFlashAttribute("mensajeExito", "Socio guardado exitosamente.");
            return "redirect:/api/socios/addSocio";
        }
    }

}