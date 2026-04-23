package com.GM2.model.domain;

import java.time.LocalDate;

/**
 * Clase de dominio que representa una Reserva en el sistema.
 * Es un POJO (Plain Old Java Object) con atributos, un constructor vacío,
 * un constructor con todos los argumentos y sus respectivos getters y setters.
 */
public class Reserva {

    // Clean Code - Regla 8: Se ha eliminado código obsoleto que estaba comentado, delegando el historial al control de versiones.
    private int id;
    private LocalDate date;
    private int seats;
    private double price;
    private String userNationalId;
    private String boatRegistration;
    private String description;

    /**
     * Constructor vacío (necesario para frameworks como Spring y serialización JSON).
     */
    public Reserva() {
    }

    /**
     * Constructor con todos los argumentos.
     */
    public Reserva(int id, LocalDate date, int seats, double price, String userNationalId, String boatRegistration, String description) {
        this.id = id;
        this.date = date;
        this.seats = seats;
        this.price = price;
        this.userNationalId = userNationalId;
        this.boatRegistration = boatRegistration;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUserNationalId() {
        return userNationalId;
    }

    public void setUserNationalId(String userNationalId) {
        this.userNationalId = userNationalId;
    }

    public String getBoatRegistration() {
        return boatRegistration;
    }

    public void setBoatRegistration(String boatRegistration) {
        this.boatRegistration = boatRegistration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
