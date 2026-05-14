package com.GM2.exception;

/**
 * Excepción lanzada cuando no se encuentra una entidad solicitada.
 */
public class EntityNotFoundException extends BaseBusinessException {

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public EntityNotFoundException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
