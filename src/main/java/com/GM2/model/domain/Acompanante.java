package com.GM2.model.domain;

/**
 * Representa a un acompanante de un alquiler de embarcación.
 * Contiene información sobre el DNI del acompanante y el ID del alquiler al que pertenece.
 * El acompanante debe ser un socio del club náutico.
 * 
 * @author gm2equipo1
 * @version 1.0
 */
public class Acompanante {
    private String dni;
    private int id_alquiler;

    /**
     * Constructor por defecto.
     * Necesario para algunas librerías (como Spring MVC) para crear instancias.
     */
    public Acompanante() {}
    
    /**
     * Constructor completo para crear un acompanante con todos sus datos.
     * 
     * @param dni DNI del acompanante
     * @param idAlquiler ID del alquiler al que pertenece
     */
    public Acompanante(String dni, int id_alquiler) {
        this.dni = dni;
        this.id_alquiler = id_alquiler;
    }

    /**
     * Obtiene el DNI del acompanante.
     * 
     * @return DNI del acompanante
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del acompanante.
     * 
     * @param dni DNI del acompanante
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el ID del alquiler al que pertenece.
     * 
     * @return ID del alquiler
     */
    public int getId_alquiler() {
        return id_alquiler;
    }

    /**
     * Establece el ID del alquiler al que pertenece.
     * 
     * @param id_alquiler ID del alquiler
     */
    public void setId_alquiler(int id_alquiler) {
        this.id_alquiler = id_alquiler;
    }
}
