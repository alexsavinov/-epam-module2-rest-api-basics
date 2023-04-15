package com.epam.esm.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TagAlreadyExistsException extends RuntimeException {
    public TagAlreadyExistsException(String message) {
        super(message);
        log.error(message);
    }
}
