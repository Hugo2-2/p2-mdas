package com.GM2.model.domain;

import java.time.LocalDate;
import java.time.Period;

/**
 * Representa un socio del club náutico.
 * Esta clase almacena la información personal de los socios,
 * incluyendo sus datos de identificación, fechas importantes y privilegios
 * como la titularidad y la licencia de patrón.
 *
 * @author gm2equipo1
 * @version 1.0
 */
public class Socio {
    private String nombre;
    private String apellidos;
    private String dni;
    private LocalDate fechaNacimiento;
    private String direccion;
    private LocalDate fechaInscripcion;
    private Boolean esTitular;
    private Boolean tieneLicenciaPatron;

    /**
     * Constructor por defecto.
     * Necesario para algunas librerías (como Spring MVC) para crear instancias.
     */
    public Socio() {}

    /**
     * Constructor completo para crear un socio con todos sus datos.
     *
     * @param tieneLicenciaPatron Indica si el socio tiene licencia de patrón.
     * @param esTitular Indica si el socio es titular de la inscripción.
     * @param fechaInscripcion Fecha de inscripción en el club.
     * @param direccion Dirección del socio.
     * @param fechaNacimiento Fecha de nacimiento del socio.
     * @param dni DNI del socio.
     * @param apellidos Apellidos del socio.
     * @param nombre Nombre del socio.
     */
    public Socio(Boolean tieneLicenciaPatron, Boolean esTitular, LocalDate fechaInscripcion, String direccion, LocalDate fechaNacimiento, String dni, String apellidos, String nombre) {
        this.tieneLicenciaPatron = tieneLicenciaPatron;
        this.esTitular = esTitular;
        this.fechaInscripcion = fechaInscripcion;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.dni = dni;
        this.apellidos = apellidos;
        this.nombre = nombre;
    }

    /**
     * Constructor para crear un socio estableciendo la fecha de inscripción como la fecha actual.
     *
     * @param tieneLicenciaPatron Indica si el socio tiene licencia de patrón.
     * @param esTitular Indica si el socio es titular de la inscripción.
     * @param direccion Dirección del socio.
     * @param fechaNacimiento Fecha de nacimiento del socio.
     * @param dni DNI del socio.
     * @param apellidos Apellidos del socio.
     * @param nombre Nombre del socio.
     */
    public Socio(Boolean tieneLicenciaPatron, Boolean esTitular, String direccion, LocalDate fechaNacimiento, String dni, String apellidos, String nombre) {
        this.tieneLicenciaPatron = tieneLicenciaPatron;
        this.esTitular = esTitular;
        this.fechaInscripcion = LocalDate.now();
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.dni = dni;
        this.apellidos = apellidos;
        this.nombre = nombre;
    }

    public Socio(String nombre, String apellidos, String dni, LocalDate fechaNacimiento, String direccion, LocalDate fechaInscripcion, Boolean esTitular, Boolean tieneLicenciaPatron) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.fechaInscripcion = fechaInscripcion;
        this.esTitular = tieneLicenciaPatron;
        this.tieneLicenciaPatron = tieneLicenciaPatron;
    }

    public Socio(String nombre, String apellidos, String dni, LocalDate fechaNacimiento, String direccion, LocalDate fechaInscripcion, Boolean tieneLicenciaPatron) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.fechaInscripcion = fechaInscripcion;
        this.tieneLicenciaPatron = tieneLicenciaPatron;
        this.esTitular = false;
    }

    // Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Boolean getEsTitular() {
        return esTitular;
    }

    public void setEsTitular(Boolean esTitular) {
        this.esTitular = esTitular;
    }

    public Boolean getTieneLicenciaPatron() {
        return tieneLicenciaPatron;
    }

    public void setTieneLicenciaPatron(Boolean tieneLicenciaPatron) {
        this.tieneLicenciaPatron = tieneLicenciaPatron;
    }

    /**
     * Determina si el socio es mayor de edad.
     * Calcula la edad actual del socio basándose en su fecha de nacimiento
     * y verifica si es mayor o igual a 18 años.
     *
     * @return true si el socio es mayor de edad (18 años o más), false en caso contrario.
     */
    public boolean esMayorEdad() {
        LocalDate hoy = LocalDate.now();
        int edad = Period.between(fechaNacimiento, hoy).getYears();
        return edad >= 18;
    }
}


