package com.GM2.model.domain;

import java.time.LocalDate;

/**
 * Clase de dominio que representa una Reserva en el sistema.
 */
public class Reserva {

    /** Precio base por plaza de reserva (en euros). */
    public static final double PRICE_PER_SEAT = 40.0;

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
