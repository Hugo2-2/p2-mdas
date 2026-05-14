package com.GM2.exception;

/**
 * Clase base abstracta para las excepciones de negocio del sistema.
 * Centraliza la estructura común (campo {@link ErrorCode}, constructores
 * con/sin argumentos dinámicos) que comparten todas las excepciones de dominio.
 *
 * Refactoring 6.3: Extract Superclass — se elimina duplicación en
 * {@link DatabaseException}, {@link EntityNotFoundException} y {@link ValidationException}.
 */
public abstract class BaseBusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    protected BaseBusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    protected BaseBusinessException(ErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
