package com.GM2.model.domain;

import java.time.LocalDate;

/**
 * Representa a los hijos asociados a una inscripción en el club náutico.
 * Esta clase almacena la información personal de los menores que forman
 * parte de una inscripción familiar, incluyendo sus datos de identificación
 * y la relación con la inscripción a la que pertenecen.
 *
 * @author gm2equipo1
 * @version 1.0
 */
public class Hijos {
    private String dni;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private int id_inscripcion;

    /**
     * Constructor completo para crear un registro de hijo con todos sus datos.
     *
     * @param dni DNI del hijo.
     * @param nombre Nombre del hijo.
     * @param apellidos Apellidos del hijo.
     * @param fechaNacimiento Fecha de nacimiento del hijo.
     * @param id_inscripcion ID de la inscripción a la que pertenece el hijo.
     */
    public Hijos(String dni, String nombre, String apellidos, LocalDate fechaNacimiento, int id_inscripcion) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.id_inscripcion = id_inscripcion;
    }

    /**
     * Constructor por defecto.
     * Necesario para algunas librerías (como Spring MVC) para crear instancias.
     */
    public Hijos() {

    }

    // Getters y Setters

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getId_inscripcion() {
        return id_inscripcion;
    }

    public void setId_inscripcion(int id_inscripcion) {
        this.id_inscripcion = id_inscripcion;
    }

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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
