package com.GM2.model.domain;

/**
 * Representa una embarcación en el club náutico.
 * Esta clase almacena la información de la flota, incluyendo sus
 * características, matrícula única y el patrón (empleado) asignado, si lo tiene.
 *
 * @author gm2equipo1
 * @version 1.0
 */
public class Embarcacion {
    private String matricula;
    private String nombre;
    private String tipo;
    private int plazas;
    private String dimensiones;
    private String idPatron;

    /**
     * Constructor por defecto.
     * Necesario para algunas librerías (como Spring MVC) para crear instancias.
     */
    public Embarcacion(){
    }

    /**
     * Constructor completo para crear una embarcación con todos sus datos.
     *
     * @param idPatron DNI del patrón asignado.
     * @param dimensiones Dimensiones de la embarcación.
     * @param plazas Número total de plazas.
     * @param tipo Tipo de embarcación.
     * @param nombre Nombre de la embarcación.
     * @param matricula Matrícula única de la embarcación.
     */
    public Embarcacion(String idPatron, String dimensiones, int plazas, String tipo, String nombre, String matricula) {
        this.idPatron = idPatron;
        this.dimensiones = dimensiones;
        this.plazas = plazas;
        this.tipo = tipo;
        this.nombre = nombre;
        this.matricula = matricula;
    }

    /**
     * Constructor para crear una embarcación sin un patrón asignado inicialmente.
     *
     * @param dimensiones Dimensiones de la embarcación.
     * @param plazas Número total de plazas.
     * @param tipo Tipo de embarcación.
     * @param nombre Nombre de la embarcación.
     * @param matricula Matrícula única de la embarcación.
     */
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
