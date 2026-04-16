package com.GM2.controller.patron;


import com.GM2.model.domain.Patron;
import com.GM2.model.repository.PatronRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador web (MVC) encargado de gestionar la creación de nuevos patrones.
 *
 * Este controlador muestra el formulario para registrar un nuevo patrón y procesa
 * el envío de datos desde la vista. Se encarga también de realizar validaciones
 * básicas relacionadas con la coherencia de las fechas y la existencia previa
 * del patrón en el sistema, antes de delegar la operación al repositorio.
 *
 */
@Controller
@RequestMapping("/api/patrones")
public class AddPatronController {

    PatronRepository patronRepository;

    /**
     * Constructor que recibe el repositorio del modelo. Siguiendo la arquitectura MVC
     * del proyecto, desde el controlador se configura el archivo de consultas SQL
     * que utilizará el repositorio para acceder a los datos persistentes.
     *
     * @param patronRepository Repositorio encargado de gestionar el acceso a datos de patrones.
     */
    public AddPatronController(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Muestra la vista correspondiente al formulario de creación de un nuevo patrón.
     * Se añade un objeto vacío de tipo {@link Patron} para que la vista pueda
     * rellenarlo mediante data binding.
     *
     * @return Un objeto ModelAndView con la vista de alta y un modelo inicializado.
     */
    @GetMapping("/addPatron")
    public ModelAndView getAddPatronView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("patron/addPatronView");
        modelAndView.addObject("newPatron", new Patron());
        return modelAndView;
    }

    /**
     * Procesa el formulario enviado desde la vista de registro de patrones.
     * En esta fase se aplican validaciones sobre:
     * - La existencia previa del patrón en la base de datos.
     * - La validez de la fecha de nacimiento.
     * - La coherencia de la fecha de expedición del título con respecto a la de nacimiento.
     *
     * Si el proceso de registro es exitoso, se informa a través de mensajes flash.
     * En caso contrario, se devuelve el error correspondiente para que la vista lo muestre.
     *
     * @param newPatron Objeto {@link Patron} rellenado desde la vista con los datos del formulario.
     * @param sessionStatus Estado de la sesión para indicar la finalización del proceso.
     * @param redirectAttributes Atributos flash utilizados para enviar mensajes entre redirecciones.
     * @return Una redirección a la misma página del formulario tras procesar los datos.
     */
    @PostMapping("/addPatron")
    public String addPatron(@ModelAttribute Patron newPatron,
                            SessionStatus sessionStatus,
                            RedirectAttributes redirectAttributes) {

        if(patronRepository.isRegistered(newPatron.getNationalId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: El patron ya existe.");
        }else {
            System.out.println("[PatronController] Informacion recivida: Nombre=" + newPatron.getName() +
                    " Apellidos=" + newPatron.getSurname() +
                    " DNI=" + newPatron.getNationalId() +
                    " fechaNacimiento=" + newPatron .getBirthDate() +
                    " fecha_expedicion_titulo=" + newPatron.getTitleIssueDate());

            if(newPatron.getBirthDate().isAfter(LocalDate.now())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Fecha de nacimiento inválida.");
                sessionStatus.setComplete();
                return "redirect:/api/patrones/addPatron";
            }

            if(newPatron.getTitleIssueDate().isAfter(LocalDate.now()) ||
                newPatron.getTitleIssueDate().isBefore(newPatron.getBirthDate())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Fecha de expedición del título inválida.");
                sessionStatus.setComplete();
                return "redirect:/api/patrones/addPatron";
            }

            boolean success = patronRepository.addPatron(newPatron);

            if(success){
                redirectAttributes.addFlashAttribute("successMessage", "Patron guardado exitosamente.");
            }
            else {
                redirectAttributes.addFlashAttribute("errorMessage", "El patron no pudo ser guardado.");
            }
        }

        sessionStatus.setComplete();
        return "redirect:/api/patrones/addPatron";
    }
}
