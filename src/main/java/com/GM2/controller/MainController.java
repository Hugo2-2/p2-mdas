package com.GM2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador web (MVC) para la página principal del sistema.
 * Maneja las peticiones web para mostrar la página principal del sistema.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
@Controller
public class MainController {

    /**
     * Muestra la página principal del sistema desde la ruta "/".
     * 
     * @return "menu" para mostrar la página de menú.
     */
    @GetMapping("/")
    public String home() {
        return "menu";
    }

    /**
     * Muestra la página de menú del sistema desde la ruta "/menu".
     * 
     * @return "menu" para mostrar la página de menú.     
     */
    @GetMapping("/menu")
    public String menu() {
        return "menu";
    }
}