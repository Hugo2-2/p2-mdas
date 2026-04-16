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

    /**
     * Obtiene el ID del alquiler.
     * 
     * @return ID del alquiler
     */
    public int getId() {
        return id;
    }
    
    /**
     * Establece el ID del alquiler.
     * 
     * @param id ID del alquiler     
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Obtiene la fecha de inicio del alquiler.
     * 
     * @return Fecha de inicio del alquiler         
     */
    public LocalDate getStartDate() {
        return startDate;
    }
    
    /**
     * Establece la fecha de inicio del alquiler.
     * 
     * @param startDate Fecha de inicio del alquiler         
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    /**
     * Obtiene la fecha de fin del alquiler.
     * 
     * @return Fecha de fin del alquiler         
     */
    public LocalDate getEndDate() {
        return endDate;
    }
    
    /**
     * Establece la fecha de fin del alquiler.
     * 
     * @param endDate Fecha de fin del alquiler         
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    /**
     * Obtiene el precio total del alquiler.
     * 
     * @return Precio total del alquiler         
     */
    public double getPrice() {
        return price;
    }
    
    /**
     * Establece el precio total del alquiler.
     * 
     * @param price Precio total del alquiler         
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /**
     * Obtiene el número de plazas reservadas.
     * 
     * @return Número de plazas reservadas         
     */
    public int getSeats() {
        return seats;
    }
    
    /**
     * Establece el número de plazas reservadas.
     * 
     * @param seats Número de plazas reservadas         
     */
    public void setSeats(int seats) {
        this.seats = seats;
    }

    /**
     * Obtiene el DNI del usuario que realiza el alquiler.
     * 
     * @return DNI del usuario         
     */
    public String getUserNationalId() {
        return userNationalId;
    }

    /**
     * Establece el DNI del usuario que realiza el alquiler.
     * 
     * @param userNationalId DNI del usuario         
     */
    public void setUserNationalId(String userNationalId) {
        this.userNationalId = userNationalId;
    }

    /**
     * Obtiene la lista de acompañantes del alquiler.
     * 
     * @return Lista de acompañantes del alquiler         
     */
    public List<Acompanante> getCompanions() {
        return companions;
    }

    /**
     * Establece la lista de acompañantes del alquiler.
     * 
     * @param companions Lista de acompañantes del alquiler         
     */
    public void setCompanions(List<Acompanante> companions) {
        this.companions = companions;
    }

    /**
     * Obtiene la matrícula de la embarcación alquilada.
     * 
     * @return Matrícula de la embarcación alquilada         
     */
    public String getBoatRegistration() {
        return boatRegistration;
    }

    /**
     * Establece la matrícula de la embarcación alquilada.
     * 
     * @param boatRegistration Matrícula de la embarcación alquilada         
     */
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
