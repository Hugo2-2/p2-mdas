package com.GM2.exception;

/**
 * Excepción lanzada cuando una operación de base de datos falla.
 */
public class DatabaseException extends BaseBusinessException {

    public DatabaseException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DatabaseException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
