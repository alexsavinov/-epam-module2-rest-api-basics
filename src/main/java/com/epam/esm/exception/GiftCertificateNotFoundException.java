package com.epam.esm.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GiftCertificateNotFoundException extends RuntimeException {

    public GiftCertificateNotFoundException(String message) {
        super(message);
        log.error(message);
    }
}
