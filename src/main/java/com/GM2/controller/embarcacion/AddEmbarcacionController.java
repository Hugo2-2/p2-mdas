package com.GM2.controller.embarcacion;

import com.GM2.model.domain.Embarcacion;
import com.GM2.model.repository.EmbarcacionRepository;

import com.GM2.model.repository.PatronRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controlador web (MVC) para la gestión de Embarcaciones.
 * * Maneja las peticiones web para mostrar formularios y procesar la
 * creación de nuevas embarcaciones. También expone algunos
 * endpoints de API (híbridos) para obtener datos en JSON
 * que fueron principalmente usados para pruebas de
 * guardado de datos.
 * * @author gm2equipo1
 * @version 3.0
 */
@Controller
@RequestMapping("/api/embarcaciones")
public class AddEmbarcacionController {

    EmbarcacionRepository embarcacionRepository;
    PatronRepository patronRepository;
    /**
     * Constructor para la inyección de dependencias.
     * Spring Boot inyectará automáticamente las instancias de los repositorios
     * y servicios necesarios.
     *
     * @param embarcacionRepository Repositorio para el acceso a datos de Embarcacion.
     * @param patronRepository Repositorio para el acceso a datos de Patron.
     * */
    public AddEmbarcacionController(EmbarcacionRepository embarcacionRepository, PatronRepository patronRepository) {
        this.embarcacionRepository = embarcacionRepository;
        this.patronRepository = patronRepository;

        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSqlQueriesFileName(sqlQueriesFileName);
        this.patronRepository.setSqlQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Muestra el formulario web (vista HTML) para añadir una nueva embarcación.
     *
     * @return Un objeto {@link ModelAndView} que contiene el nombre de la vista
     * (addEmbarcacionView) y un objeto Embarcacion vacío para el formulario.
     */
    @GetMapping("/addEmbarcacion")
    public ModelAndView getAddEmbarcacionView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("embarcacion/addEmbarcacionView");
        modelAndView.addObject("newEmbarcacion", new Embarcacion());
        return modelAndView;
    }

    /**
     * Procesa el envío del formulario (POST) para añadir una nueva embarcacion.
     * Utiliza el patrón Post-Redirect-Get para evitar envíos duplicados.
     *
     * @param newEmbarcacion     El objeto Embarcacion con los datos rellenados del formulario (@ModelAttribute).
     * @param sessionStatus      Controlador de estado de la sesión para limpiarla tras el envío.
     * @param redirectAttributes Interfaz para pasar mensajes (éxito/error) a la vista de redirección.
     * @return Un String que indica la URL a la que se debe redirigir.
     */
    @PostMapping("/addEmbarcacion")
    public String addEmbarcacion(@ModelAttribute Embarcacion newEmbarcacion,
                                 SessionStatus sessionStatus,
                                 RedirectAttributes redirectAttributes){

        System.out.println("[EmbarcacionController] Informacion recivida: matricula=" + newEmbarcacion.getMatricula() +
                " nombre=" + newEmbarcacion.getNombre() +
                " tipo=" + newEmbarcacion.getTipo() +
                " dimensiones=" + newEmbarcacion.getDimensiones() +
                " id_patron=" + newEmbarcacion.getIdPatron() +
                " plazas=" + newEmbarcacion.getPlazas());

        // 1. Comprobamos que la matrícula sea única, al igual con el nombre
        if (embarcacionRepository.findEmbarcacionByMatricula(newEmbarcacion.getMatricula()) != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Ya existe una embarcación con esa matrícula");
        } else if (embarcacionRepository.findEmbarcacionByNombre(newEmbarcacion.getNombre()) != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Ya existe una embarcación con dicho nombre asociado");
        }
        // 2. Comprobamos que al menos sea 2 plazas (1 para patron y otra para quien reserva)
        else if (newEmbarcacion.getPlazas() < 2) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: El número mínimo de plazas debe ser 2");
        }
        // 3. Validación de dimensiones, se mete en un try catch, ya que hay que pasar de string a double las dimensiones
        else try {
                double dimensiones = Double.parseDouble(newEmbarcacion.getDimensiones());
                if (dimensiones < 1) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Error: La embarcación debe tener al menos 1m2");
                }

                // 4. Validación de Patrón
                String patronDni = newEmbarcacion.getIdPatron();
                //El dni del patron no debe estar nulo, ni dejarse vacío
                if (patronDni != null && !patronDni.trim().isEmpty()) {
                    if (patronRepository.findPatronByDNI(patronDni) == null) {
                        redirectAttributes.addFlashAttribute("errorMessage", "Error: El patron no existe");
                    } else if (embarcacionRepository.isPatronAssignedToEmbarcacion(patronDni)) {
                        redirectAttributes.addFlashAttribute("errorMessage", "Error: El patron ya se encuentra asignado a una embarcación");
                    }
                }

                // 5. Insertamos los datos
                boolean resultado = embarcacionRepository.addEmbarcacion(newEmbarcacion); // Llama al método "tonto"
                if (resultado) {
                    redirectAttributes.addFlashAttribute("successMessage", "La embarcacion se ha ingresado correctamente");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Error: No se pudo añadir la embarcación");
                }
            } catch (NumberFormatException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: El formato de las dimensiones no es válido.");
            }

        sessionStatus.setComplete();
        return "redirect:/api/embarcaciones/addEmbarcacion";
    }
}
