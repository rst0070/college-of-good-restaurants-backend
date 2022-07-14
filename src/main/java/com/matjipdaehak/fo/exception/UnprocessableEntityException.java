package com.matjipdaehak.fo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * error code : 422
 *
 */
public class UnprocessableEntityException extends ResponseStatusException {

    public UnprocessableEntityException(String reason) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason);
    }
}
