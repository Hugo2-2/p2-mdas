package com.GM2.controller.Socio;

import com.GM2.model.domain.Alquiler;
import com.GM2.model.domain.Hijos;
import com.GM2.model.domain.Socio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/api/socios")
public class SocioController {

    SocioService socioService;


    public SocioController(SocioService socioService) { this.socioService = socioService; }

    @GetMapping
    @ResponseBody
    public List<Socio> getSocios() { return socioService.findAll(); }

    @GetMapping("/addSocio")
    public ModelAndView mostrarFormularioSocio() {
        ModelAndView mv = new ModelAndView("addSocioView");
        mv.addObject("socio", new Socio());
        return mv;
    }

    @PostMapping("/addSocio")
    public String addSocio(@ModelAttribute Socio socio, RedirectAttributes redirectAttributes, Model model, @RequestParam String segundoAdultoDNI) {

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
        String mensaje = socioService.addSocio(socio);

        //Evaluamos el mensaje que se mostrará en las flashcards de error
        if(mensaje.equals("EXITO")) {
            redirectAttributes.addFlashAttribute("mensaje", "Socio guardado exitosamente");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", mensaje);
        }

        //Volvemos al formulario vacio
        return "/api/socios/addIncripcion";
    }


}
