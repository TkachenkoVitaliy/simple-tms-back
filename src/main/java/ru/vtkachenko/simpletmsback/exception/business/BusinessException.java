package ru.vtkachenko.simpletmsback.exception.business;

import org.springframework.http.HttpStatus;


public abstract class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);

    }

    public abstract int getCode();

    public abstract String getMessageCode();

    public abstract HttpStatus getStatusCode();
}
