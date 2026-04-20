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

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public int getId() {
        return id;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setId(int id) {
        this.id = id;
    }
    
    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public String getNationalId() {
        return nationalId;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
    public int getRentalId() {
        return rentalId;
    }

    //Clean Code - Reglas de comentarios: Comentario redundate sobre getters y setters
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
