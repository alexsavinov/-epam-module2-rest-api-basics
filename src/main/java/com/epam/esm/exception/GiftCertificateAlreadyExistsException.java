package com.epam.esm.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GiftCertificateAlreadyExistsException extends RuntimeException {

    public GiftCertificateAlreadyExistsException(String message) {
        super(message);
        log.error(message);
    }
}
