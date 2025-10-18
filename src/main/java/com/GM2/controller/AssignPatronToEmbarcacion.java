package com.GM2.controller;

import com.GM2.model.repository.EmbarcacionRepository;
import com.GM2.model.repository.PatronRepository;
import org.springframework.stereotype.Service;

@Service
public class AssignPatronToEmbarcacion {
    EmbarcacionRepository embarcacionRepository;
    PatronRepository patronRepository;

    public AssignPatronToEmbarcacion(EmbarcacionRepository embarcacionRepository, PatronRepository patronRepository) {
        this.embarcacionRepository = embarcacionRepository;
        this.patronRepository = patronRepository;
    }

    public String asociatePatron(String matricula, String patronDni) {
        // 1. Verificar que el patrón existe
        if (patronRepository.findPatronByDNI(patronDni) == null) {
            return "Error: El patrón con DNI " + patronDni + " no existe.";
        }

        // 2. Verificar si el patrón ya está asignado a OTRA embarcación [cite: 991]
        if (embarcacionRepository.isPatronAssignedToEmbarcacion(patronDni)) {
            return "Error: El patrón ya está asignado a otra embarcación.";
        }

        // 3. Obtener el patrón actual de la embarcación [cite: 986]
        String patronActual = embarcacionRepository.getPatronAssignedToEmbarcacion(matricula);

        // 4. Ejecutar la asignación
        embarcacionRepository.updatePatron(patronDni, matricula);

        if (patronActual != null) {
            // Informar del reemplazo [cite: 986, 992]
            return "Reemplazo exitoso. Patrón " + patronActual + " ahora está libre.";
        } else {
            return "Patrón " + patronDni + " asignado correctamente.";
        }
    }
}
