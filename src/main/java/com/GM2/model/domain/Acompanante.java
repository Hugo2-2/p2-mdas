package com.GM2.model.domain;

/**
 * Representa a un acompanante de un alquiler de embarcación.
 * Contiene información sobre el DNI del acompanante y el ID del alquiler al que pertenece, además de su ID representativo.
 * El acompanante debe ser un socio del club náutico.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
public class Acompanante {
    private int id;
    private String nationalId;
    private int rentalId;

    /**
     * Constructor por defecto.
     * Necesario para algunas librerías (como Spring MVC) para crear instancias.
     */
    public Acompanante() {}
    
    /**
     * Constructor completo para crear un acompanante con todos sus datos.
     * 
     * @param id ID del acompanante
     * @param nationalId DNI del acompanante
     * @param rentalId ID del alquiler al que pertenece
     */
    public Acompanante(int id, String nationalId, int rentalId) {
        this.id = id;
        this.nationalId = nationalId;
        this.rentalId = rentalId;
    }

    /**
     * Obtiene el ID del acompanante.
     * 
     * @return ID del acompanante
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del acompanante.
     * 
     * @param id ID del acompanante
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Obtiene el DNI del acompanante.
     * 
     * @return DNI del acompanante
     */
    public String getNationalId() {
        return nationalId;
    }

    /**
     * Establece el DNI del acompanante.
     * 
     * @param nationalId DNI del acompanante
     */
    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    /**
     * Obtiene el ID del alquiler al que pertenece.
     * 
     * @return ID del alquiler
     */
    public int getRentalId() {
        return rentalId;
    }

    /**
     * Establece el ID del alquiler al que pertenece.
     * 
     * @param rentalId ID del alquiler
     */
    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }


    /**
     * Representación de un acompanante en String.
     * 
     * @return Representación de un acompanante en String
     */
    @Override
    public String toString() {
        return "DNI='" + nationalId + "'";
    }
}
