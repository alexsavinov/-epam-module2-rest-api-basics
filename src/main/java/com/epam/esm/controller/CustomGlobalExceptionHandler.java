package com.epam.esm.controller;

import com.epam.esm.exception.GiftCertificateAlreadyExistsException;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.TagAlreadyExistsException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.entity.CustomErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private HttpStatus httpStatus;
    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleTagNotFound(Exception ex, WebRequest request) {
        httpStatus = HttpStatus.NOT_FOUND;

        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setErrorMessage(ex.getMessage());
        errors.setErrorCode(Integer.parseInt(httpStatus.value() + "01"));

        return new ResponseEntity<>(errors, httpStatus);
    }
    @ExceptionHandler(TagAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handleTagAlreadyExists(Exception ex, WebRequest request) {
        httpStatus = HttpStatus.CONFLICT;

        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setErrorMessage(ex.getMessage());
        errors.setErrorCode(Integer.parseInt(httpStatus.value() + "01"));

        return new ResponseEntity<>(errors, httpStatus);
    }
    @ExceptionHandler(GiftCertificateNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleGiftCertificateNotFound(Exception ex, WebRequest request) {
        httpStatus = HttpStatus.NOT_FOUND;

        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setErrorMessage(ex.getMessage());
        errors.setErrorCode(Integer.parseInt(httpStatus.value() + "02"));

        return new ResponseEntity<>(errors, httpStatus);
    }
    @ExceptionHandler(GiftCertificateAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handleGiftCertificateAlreadyExists(Exception ex, WebRequest request) {
        httpStatus = HttpStatus.CONFLICT;

        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setErrorMessage(ex.getMessage());
        errors.setErrorCode(Integer.parseInt(httpStatus.value() + "02"));

        return new ResponseEntity<>(errors, httpStatus);
    }
}