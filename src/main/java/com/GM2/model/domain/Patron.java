package com.GM2.model.domain;

import java.time.LocalDate;

public class Patron {
    private String id;
    private String nombre;
    private String apellidos;
    private String dni;
    private LocalDate fechaNacimiento;
    private LocalDate fechaExpedicionTitulo;

    public Patron(String id, String nombre, String apellidos, String dni, LocalDate fechaNacimiento, LocalDate fechaExpedicionTitulo) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaExpedicionTitulo = fechaExpedicionTitulo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocalDate getFechaExpedicionTitulo() {
        return fechaExpedicionTitulo;
    }

    public void setFechaExpedicionTitulo(LocalDate fechaExpedicionTitulo) {
        this.fechaExpedicionTitulo = fechaExpedicionTitulo;
    }
}
