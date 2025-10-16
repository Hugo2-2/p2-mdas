package com.GM2.model.domain;

public class Acompañantes {
    private String dni;
    private int id_alquiler;

    public Acompañantes(String dni, int id_alquiler) {
        this.dni = dni;
        this.id_alquiler = id_alquiler;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getId_alquiler() {
        return id_alquiler;
    }

    public void setId_alquiler(int id_alquiler) {
        this.id_alquiler = id_alquiler;
    }
}
