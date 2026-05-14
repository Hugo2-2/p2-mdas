package com.GM2.exception;

/**
 * Excepción lanzada cuando una validación de negocio falla.
 */
public class ValidationException extends BaseBusinessException {

    public ValidationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ValidationException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
