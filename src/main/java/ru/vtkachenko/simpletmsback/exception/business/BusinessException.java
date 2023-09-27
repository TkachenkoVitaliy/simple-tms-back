package ru.vtkachenko.simpletmsback.exception.business;

import org.springframework.http.HttpStatus;
import ru.vtkachenko.simpletmsback.dto.ErrorInfo;

public abstract class BusinessException extends RuntimeException {
    public BusinessException(String message, int code, String messageCode, HttpStatus statusCode) {
        super(message);
        this.code = code;
        this.messageCode = messageCode;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String messageCode;

    private final HttpStatus statusCode;

    public ErrorInfo getClientInfo() {
        return new ErrorInfo(code, messageCode);
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
