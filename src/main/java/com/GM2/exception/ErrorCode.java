package com.GM2.exception;

/**
 * Enumeración de códigos de error del sistema.
 * Centraliza todos los mensajes de error para evitar strings hardcodeados
 * dispersos por el código y facilitar su mantenimiento sin recompilar los
 * módulos que los usan.
 */
public enum ErrorCode {

    // --- Inscripción ---
    INSCRIPCION_NO_INGRESADA("No se ha podido ingresar la inscripcion"),
    INSCRIPCION_NO_EXISTE("No puedes actualizar la inscripcion porque no existe"),
    INSCRIPCION_NO_ACTUALIZADA("No se ha podido actualizar la inscripcion"),
    TITULAR_NO_ES_TITULAR_INSCRIPCION("El socio introducido como titular no es titular de ninguna inscripción"),

    // --- Socios en inscripción ---
    TITULAR_Y_SEGUNDO_ADULTO_NO_SOCIOS("El titular y el segundo adulto no están registrados como socios"),
    TITULAR_NO_SOCIO("El titular no está registrado como socio"),
    SEGUNDO_ADULTO_NO_SOCIO("El segundo adulto no está registrado como socio"),

    // --- Hijos ---
    HIJO_DNI_INVALIDO("DNI de hijo no válido o vacío"),
    HIJO_NOMBRE_NO_PROPORCIONADO("Nombre de hijo no proporcionado para DNI: %s"),
    HIJO_APELLIDOS_NO_PROPORCIONADOS("Apellidos de hijo no proporcionados para DNI: %s"),
    HIJO_FECHA_NO_PROPORCIONADA("Fecha de nacimiento no proporcionada para DNI: %s"),
    HIJO_NO_GUARDADO("Error al guardar el hijo con DNI: %s"),

    // --- Socio ---
    SOCIO_NO_INGRESADO("No se ha ingresado el socio"),
    SOCIO_DEBE_SER_MAYOR_DE_EDAD("Debes de ser mayor de edad para realizar esta inscripcion"),
    SOCIO_NO_GUARDADO("No se ha podido guardar el socio"),
    SOCIO_DNI_OBLIGATORIO("El DNI es obligatorio para actualizar el socio"),
    SOCIO_NO_EXISTE("No se puede actualizar, el socio no existe"),
    SOCIO_NO_ACTUALIZADO("No se ha podido actualizar el socio");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    /** Devuelve el mensaje de error sin argumentos dinámicos. */
    public String getMessage() {
        return message;
    }

    /**
     * Devuelve el mensaje de error con argumentos dinámicos interpolados
     * mediante {@link String#format(String, Object...)}.
     *
     * @param args valores a sustituir en el patrón del mensaje
     */
    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
