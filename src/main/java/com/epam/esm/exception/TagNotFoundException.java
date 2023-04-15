package com.epam.esm.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(String message) {
        super(message);
        log.error(message);
    }
}
