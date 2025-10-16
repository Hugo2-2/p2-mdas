package com.GM2.model.domain;

import java.time.LocalDate;
import java.util.Date;

public class Socio {
    private String nombre;
    private String apellidos;
    private String dni;
    private LocalDate fechaNacimiento;
    private String direccion;
    private LocalDate fechaInscripcion;
    private Boolean esTitular;
    private Boolean tieneLicenciaPatron;

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
}
