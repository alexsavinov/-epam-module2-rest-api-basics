package com.epam.esm.controller;

import com.epam.esm.exception.GiftCertificateAlreadyExistsException;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.TagAlreadyExistsException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.entity.CustomErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TagNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomErrorResponse handleTagNotFound(Exception ex) {
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setErrorMessage(ex.getMessage());
        errors.setErrorCode(40401);

        return errors;
    }

    @ExceptionHandler(TagAlreadyExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public CustomErrorResponse handleTagAlreadyExists(Exception ex) {
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setErrorMessage(ex.getMessage());
        errors.setErrorCode(40901);

        return errors;
    }

    @ExceptionHandler(GiftCertificateNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomErrorResponse handleGiftCertificateNotFound(Exception ex) {
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setErrorMessage(ex.getMessage());
        errors.setErrorCode(40402);

        return errors;
    }

    @ExceptionHandler(GiftCertificateAlreadyExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public CustomErrorResponse handleGiftCertificateAlreadyExists(Exception ex) {
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setErrorMessage(ex.getMessage());
        errors.setErrorCode(40902);

        return errors;
    }
}