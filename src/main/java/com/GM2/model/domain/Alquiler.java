package com.GM2.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un alquiler de embarcación en el sistema del Club Náutico.
 * Contiene información sobre las fechas, plazas, precios y relaciones con socios y embarcaciones.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
public class Alquiler {
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
     * Constructor con todos los parámetros.
     * 
     * @param id ID del alquiler
     * @param startDate Fecha de inicio del alquiler
     * @param endDate Fecha de fin del alquiler
     * @param price Precio total del alquiler
     * @param seats Número de plazas reservadas
     * @param userNationalId DNI del usuario que realiza el alquiler
     * @param boatRegistration Matrícula de la embarcación alquilada
     * @param companions Lista de acompañantes
     */
    public Alquiler(int id, LocalDate startDate, LocalDate endDate, double price, int seats, String userNationalId, String boatRegistration, List<Acompanante> companions) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.seats = seats;
        this.userNationalId = userNationalId;
        this.boatRegistration = boatRegistration;
        this.companions = companions;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public int getId() {
        return id;
    }
    
    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setId(int id) {
        this.id = id;
    }
    
    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public LocalDate getStartDate() {
        return startDate;
    }
    
    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public LocalDate getEndDate() {
        return endDate;
    }
    
    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public double getPrice() {
        return price;
    }
    
    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setPrice(double price) {
        this.price = price;
    }
    
    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public int getSeats() {
        return seats;
    }
    
     //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setSeats(int seats) {
        this.seats = seats;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public String getUserNationalId() {
        return userNationalId;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setUserNationalId(String userNationalId) {
        this.userNationalId = userNationalId;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public List<Acompanante> getCompanions() {
        return companions;
    }

     //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setCompanions(List<Acompanante> companions) {
        this.companions = companions;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public String getBoatRegistration() {
        return boatRegistration;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setBoatRegistration(String boatRegistration) {
        this.boatRegistration = boatRegistration;
    }


    /**
     * Representación de un alquiler en String.
     * 
     * @return Representación de un alquiler en String
     */
    @Override
    public String toString() {
        String result = "Alquiler {\n";
        result += "  ID: " + id + "\n";
        result += "  Fecha inicio: " + startDate + "\n";
        result += "  Fecha fin: " + endDate + "\n";
        result += "  Plazas: " + seats + "\n";
        result += "  Precio: " + price + "€\n";
        result += "  DNI Socio: " + userNationalId + "\n";
        result += "  Matrícula: " + boatRegistration + "\n";
        
        if (companions != null && !companions.isEmpty()) {
            result += "  Acompañantes (" + companions.size() + "):\n";
            for (Acompanante a : companions) {
                result += "    - " + a + "\n"; // Llama a a.toString()
            }
        } else {
            result += "  Acompañantes: Ninguno\n";
        }
        
        result += "}";
        return result;
    }   
}
