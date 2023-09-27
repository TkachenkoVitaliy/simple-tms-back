package ru.vtkachenko.simpletmsback.controller;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.vtkachenko.simpletmsback.dto.ErrorInfo;
import ru.vtkachenko.simpletmsback.i18n.I18n;
import ru.vtkachenko.simpletmsback.i18n.I18nPackage;

@Slf4j
@RestControllerAdvice
public class ApplicationAdvice {

    private final I18n i18n;

    public ApplicationAdvice(I18n i18n) {
        this.i18n = i18n;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorInfo handleConstraintException(ConstraintViolationException e) {
        String constraintName = e.getConstraintName();
        return new ErrorInfo(1000, i18n.translate(constraintName, I18nPackage.ERROR));
    }
}
