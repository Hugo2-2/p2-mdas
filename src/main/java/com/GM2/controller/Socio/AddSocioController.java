package com.GM2.controller.Socio;

import com.GM2.model.domain.Socio;
import com.GM2.model.repository.SocioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/**
 * Controlador web (MVC) para la gestión de Socios.
 * Maneja las peticiones web para mostrar formularios y procesar la
 * creación de nuevos socios. Incluye lógica para manejar inscripciones
 * automáticas y redirección a formularios de inscripción familiar
 * cuando el socio es titular.
 *
 * @author gm2equipo1
 * @version 1.0
 */
@Controller
@RequestMapping("/api/socios")
public class AddSocioController {

    SocioRepository socioRepository;

    /**
     * Constructor para la inyección de dependencias.
     * Spring Boot inyectará automáticamente las instancias de los repositorios
     * y servicios necesarios.
     *
     * @param socioRepository Repositorio para el acceso a datos de Socio.
     */
    public AddSocioController(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.socioRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Muestra el formulario web (vista HTML) para añadir un nuevo socio.
     *
     * @return Un objeto {@link ModelAndView} que contiene el nombre de la vista
     *         (addSocioView) y un objeto Socio vacío para el formulario.
     */
    @GetMapping("/addSocio")
    public ModelAndView mostrarFormularioSocio() {
        ModelAndView mv = new ModelAndView("addSocioView");
        mv.addObject("socio", new Socio());
        return mv;
    }

    /**
     * Procesa el envío del formulario (POST) para añadir un nuevo socio.
     * Utiliza el patrón Post-Redirect-Get para evitar envíos duplicados.
     * Maneja la lógica de creación de inscripciones automáticas y redirección
     * a formularios de inscripción familiar si el socio es titular.
     *
     * @param socio El objeto Socio con los datos rellenados del formulario (@ModelAttribute).
     * @param ampliarInscripcion Parámetro opcional que indica si se desea ampliar la inscripción.
     * @param redirectAttributes Interfaz para pasar mensajes (éxito/error) a la vista de redirección.
     * @return Un String que indica la URL a la que se debe redirigir.
     */
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