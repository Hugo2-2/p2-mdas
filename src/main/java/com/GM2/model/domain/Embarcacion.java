package com.GM2.model.domain;

import java.time.LocalDate;

/**
 * Representa una embarcación en el club náutico.
 * Esta clase almacena la información de la flota, incluyendo sus
 * características, matrícula única y el patrón (empleado) asignado, si lo tiene.
 *
 * @author gm2equipo1
 * @version 1.0
 */
public class Embarcacion {
    private String registration;
    private String name;
    private String type;
    private int seats;
    private String dimensions;
    private String skipperId;

    /**
     * Constructor por defecto.
     * Necesario para algunas librerías (como Spring MVC) para crear instancias.
     */
    public Embarcacion(){
    }

    /**
     * Constructor completo para crear una embarcación con todos sus datos.
     *
     * @param skipperId DNI del patrón asignado.
     * @param dimensions Dimensiones de la embarcación (en m2).
     * @param seats Número total de plazas.
     * @param type Tipo de embarcación.
     * @param name Nombre de la embarcación.
     * @param registration Matrícula única de la embarcación.
     */
    public Embarcacion(String skipperId, String dimensions, int seats, String type, String name, String registration) {
        this.skipperId = skipperId;
        this.dimensions = dimensions;
        this.seats = seats;
        this.type = type;
        this.name = name;
        this.registration = registration;
    }

    /**
     * Constructor para crear una embarcación sin un patrón asignado inicialmente.
     *
     * @param dimensions Dimensiones de la embarcación (en m2).
     * @param seats Número total de plazas.
     * @param type Tipo de embarcación.
     * @param name Nombre de la embarcación.
     * @param registration Matrícula única de la embarcación.
     */
    public Embarcacion(String dimensions, int seats, String type, String name, String registration) {
        this.skipperId = ""; // Asigna un string vacío como valor por defecto
        this.dimensions = dimensions;
        this.seats = seats;
        this.type = type;
        this.name = name;
        this.registration = registration;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public String getSkipperId() {
        return skipperId;
    }

      //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setSkipperId(String skipperId) {
        this.skipperId = skipperId;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public String getDimensions() {
        return dimensions;
    }

     //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public int getSeats() {
        return seats;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setSeats(int seats) {
        this.seats = seats;
    }

     //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public String getType() {
        return type;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setType(String type) {
        this.type = type;
    }

     //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public String getName() {
        return name;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setName(String name) {
        this.name = name;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public String getRegistration() {
        return registration;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setRegistration(String registration) {
        this.registration = registration;
    }

    /**
     * Devuelve una representación en forma de cadena de texto de la embarcación.
     * Este método es útil para la depuración y el registro (logging), mostrando
     * los atributos principales como matrícula, nombre, tipo, plazas, dimensiones
     * y el identificador del patrón asignado.
     *
     * @return Una cadena (String) con los detalles de la embarcación.
     */
    @Override
    public String toString() {
        return "Embarcacion {" +
                "matricula='" + registration + '\'' +
                ", nombre='" + name + '\'' +
                ", tipo='" + type + '\'' +
                ", plazas=" + seats +
                ", dimensiones='" + dimensions + '\'' +
                ", idPatron='" + (skipperId != null ? skipperId : "Ninguno") + '\'' +
                '}';
    }
}