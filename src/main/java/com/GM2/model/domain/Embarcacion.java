package com.GM2.model.domain;

public class Embarcacion {
    private String matricula;
    private String nombre;
    private String tipo;
    private int plazas;
    private String dimensiones;
    private String idPatron;

    public Embarcacion(){
    }

    public Embarcacion(String idPatron, String dimensiones, int plazas, String tipo, String nombre, String matricula) {
        this.idPatron = idPatron;
        this.dimensiones = dimensiones;
        this.plazas = plazas;
        this.tipo = tipo;
        this.nombre = nombre;
        this.matricula = matricula;
    }

    public Embarcacion(String dimensiones, int plazas, String tipo, String nombre, String matricula) {
        this.idPatron = "";
        this.dimensiones = dimensiones;
        this.plazas = plazas;
        this.tipo = tipo;
        this.nombre = nombre;
        this.matricula = matricula;
    }


    public String getIdPatron() {
        return idPatron;
    }

    public void setIdPatron(String idPatron) {
        this.idPatron = idPatron;
    }

    public String getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(String dimensiones) {
        this.dimensiones = dimensiones;
    }

    public int getPlazas() {
        return plazas;
    }

    public void setPlazas(int plazas) {
        this.plazas = plazas;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
