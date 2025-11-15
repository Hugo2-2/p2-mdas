package com.GM2.model.domain;

import java.time.LocalDate; // Importa la clase para manejar fechas sin hora (inmutable)

/**
 * Clase de dominio que representa una Reserva en el sistema.
 * Es un POJO (Plain Old Java Object) con atributos, un constructor vacío,
 * un constructor con todos los argumentos y sus respectivos getters y setters.
 */
public class Reserva {

    // --- Atributos de la Reserva (corresponden a las columnas de la tabla de BD) ---

    private int id;
    private LocalDate fecha;
    private int plazas;
    private double precio;
    private String usuario_id;
    private String matricula_embarcacion;
    private String descripcion;

    /**
     * Constructor vacío (necesario para frameworks como Spring y serialización JSON).
     */
    public Reserva() {
    }

    /**
     * Constructor con todos los argumentos.
     */
    public Reserva(int id, LocalDate fecha, int plazas, double precio, String usuario_id, String matricula_embarcacion, String descripcion) {
        this.id = id;
        this.fecha = fecha;
        this.plazas = plazas;
        this.precio = precio;
        this.usuario_id = usuario_id;
        this.matricula_embarcacion = matricula_embarcacion;
        this.descripcion = descripcion;
    }

    // --- Métodos Getters y Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getPlazas() {
        return plazas;
    }

    public void setPlazas(int plazas) {
        this.plazas = plazas;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getMatricula_embarcacion() {
        return matricula_embarcacion;
    }

    public void setMatricula_embarcacion(String matricula_embarcacion) {
        this.matricula_embarcacion = matricula_embarcacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
