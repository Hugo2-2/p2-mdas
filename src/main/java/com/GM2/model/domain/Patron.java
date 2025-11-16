package com.GM2.model.domain;

import java.time.LocalDate;

/**
 * Clase que representa a un patrón en el sistema.
 * Contiene los datos personales y la información relevante sobre su título.
 *
 * Cada instancia de esta clase corresponde a un patrón que puede estar
 * asociado a embarcaciones en la aplicación.
 */
public class Patron {

    private String nombre;
    private String apellidos;
    private String dni;
    private LocalDate fechaNacimiento;
    private LocalDate fechaExpedicionTitulo;

    /**
     * Constructor vacío.
     * Necesario para frameworks como Spring al crear instancias mediante reflexión.
     */
    public Patron() {
    }

    /**
     * Constructor completo.
     * Permite crear un patrón con toda su información básica.
     *
     * @param nombre Nombre del patrón
     * @param apellidos Apellidos del patrón
     * @param dni DNI del patrón (identificador único)
     * @param fechaNacimiento Fecha de nacimiento
     * @param fechaExpedicionTitulo Fecha de expedición del título de patrón
     */
    public Patron(String nombre, String apellidos, String dni, LocalDate fechaNacimiento, LocalDate fechaExpedicionTitulo) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaExpedicionTitulo = fechaExpedicionTitulo;
    }

    /** Getter del nombre */
    public String getNombre() {
        return nombre;
    }

    /** Setter del nombre */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** Getter de los apellidos */
    public String getApellidos() {
        return apellidos;
    }

    /** Setter de los apellidos */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /** Getter del DNI */
    public String getDni() {
        return dni;
    }

    /** Setter del DNI */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /** Getter de la fecha de nacimiento */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /** Setter de la fecha de nacimiento */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /** Getter de la fecha de expedición del título */
    public LocalDate getFechaExpedicionTitulo() {
        return fechaExpedicionTitulo;
    }

    /** Setter de la fecha de expedición del título */
    public void setFechaExpedicionTitulo(LocalDate fechaExpedicionTitulo) {
        this.fechaExpedicionTitulo = fechaExpedicionTitulo;
    }
}
