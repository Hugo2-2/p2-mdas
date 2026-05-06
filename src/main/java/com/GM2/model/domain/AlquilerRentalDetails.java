package com.GM2.model.domain;

import java.time.LocalDate;

/**
 * DTO que encapsula los detalles operativos de un alquiler de embarcación.
 * Agrupa la información de periodo, capacidad, precio y referencias al socio
 * y la embarcación en un único objeto con cohesión lógica, permitiendo
 * reducir el número de argumentos en los constructores de {@link Alquiler}.
 *
 * @author gm2equipo1
 * @version 1.0
 */
public class AlquilerRentalDetails {

    private LocalDate startDate;
    private LocalDate endDate;
    private double price;
    private int seats;
    private String userNationalId;
    private String boatRegistration;

    /**
     * Constructor por defecto.
     * Necesario para algunas librerías (como Spring MVC) para crear instancias.
     */
    public AlquilerRentalDetails() {
    }

    /**
     * Constructor con todos los parámetros del detalle de alquiler.
     *
     * @param startDate Fecha de inicio del alquiler.
     * @param endDate Fecha de fin del alquiler.
     * @param price Precio total del alquiler.
     * @param seats Número de plazas reservadas.
     * @param userNationalId DNI del usuario que realiza el alquiler.
     * @param boatRegistration Matrícula de la embarcación alquilada.
     */
    public AlquilerRentalDetails(LocalDate startDate, LocalDate endDate, double price, int seats, String userNationalId, String boatRegistration) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.seats = seats;
        this.userNationalId = userNationalId;
        this.boatRegistration = boatRegistration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
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
}
