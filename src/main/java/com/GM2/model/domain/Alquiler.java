package com.GM2.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa un alquiler de embarcación en el sistema del Club Náutico.
 * Contiene información sobre las fechas, plazas, precios y relaciones con socios y embarcaciones.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
public class Alquiler {

    /** Precio base por persona y día de alquiler (en euros). */
    public static final double PRICE_PER_PERSON_PER_DAY = 20.0;

    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private double price;
    private int seats;
    private String userNationalId;
    private String boatRegistration;
    private List<Acompanante> companions;

    /**
     * Constructor por defecto.
     * Necesario para algunas librerías (como Spring MVC) para crear instancias.
     */
    public Alquiler() {
        this.companions = new ArrayList<>();
    }

    /**
     * Constructor con todos los parámetros, refactorizado para recibir un objeto
     * {@link AlquilerRentalDetails} en lugar de una lista larga de argumentos.
     * 
     * @param id ID del alquiler
     * @param rentalDetails Objeto que encapsula los detalles del alquiler (fechas, precio, plazas, socio, embarcación)
     * @param companions Lista de acompañantes
     */
    public Alquiler(int id, AlquilerRentalDetails rentalDetails, List<Acompanante> companions) {
        this.id = id;
        this.startDate = rentalDetails.getStartDate();
        this.endDate = rentalDetails.getEndDate();
        this.price = rentalDetails.getPrice();
        this.seats = rentalDetails.getSeats();
        this.userNationalId = rentalDetails.getUserNationalId();
        this.boatRegistration = rentalDetails.getBoatRegistration();
        this.companions = companions != null ? new ArrayList<>(companions) : new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    /**
     * Devuelve una vista no modificable de la lista de acompañantes.
     * Para modificar la lista, use {@link #addCompanion(Acompanante)} o {@link #removeCompanion(Acompanante)}.
     */
    public List<Acompanante> getCompanions() {
        return Collections.unmodifiableList(companions);
    }

    public void setCompanions(List<Acompanante> companions) {
        this.companions = companions != null ? new ArrayList<>(companions) : new ArrayList<>();
    }

    public void addCompanion(Acompanante companion) {
        this.companions.add(companion);
    }

    public void removeCompanion(Acompanante companion) {
        this.companions.remove(companion);
    }

    public String getBoatRegistration() {
        return boatRegistration;
    }

    public void setBoatRegistration(String boatRegistration) {
        this.boatRegistration = boatRegistration;
    }

    /**
     * Representación de un alquiler en String.
     * Refactorizado para usar StringBuilder en lugar de concatenación con +=.
     * 
     * @return Representación de un alquiler en String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Alquiler {\n");
        sb.append("  ID: ").append(id).append("\n");
        sb.append("  Fecha inicio: ").append(startDate).append("\n");
        sb.append("  Fecha fin: ").append(endDate).append("\n");
        sb.append("  Plazas: ").append(seats).append("\n");
        sb.append("  Precio: ").append(price).append("€\n");
        sb.append("  DNI Socio: ").append(userNationalId).append("\n");
        sb.append("  Matrícula: ").append(boatRegistration).append("\n");

        if (companions != null && !companions.isEmpty()) {
            sb.append("  Acompañantes (").append(companions.size()).append("):\n");
            for (Acompanante a : companions) {
                sb.append("    - ").append(a).append("\n");
            }
        } else {
            sb.append("  Acompañantes: Ninguno\n");
        }

        sb.append("}");
        return sb.toString();
    }
}
