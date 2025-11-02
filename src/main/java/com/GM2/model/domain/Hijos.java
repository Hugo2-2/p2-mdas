package com.GM2.model.domain;

public class Hijos {
    private String dni;
    private int id_inscripcion;

    public Hijos(String dni, int id_inscripcion) {
        this.dni = dni;
        this.id_inscripcion = id_inscripcion;
    }

    public Hijos() {

    }

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

}
