package ru.vtkachenko.simpletmsback.exception.business;

import org.springframework.http.HttpStatus;
import ru.vtkachenko.simpletmsback.exception.enums.ErrorEntity;

public class BadRequestException extends BusinessException {
    private static final int CODE = 400;
    private static final String MESSAGE_CODE = "bad_request";
    private static final HttpStatus STATUS_CODE = HttpStatus.BAD_REQUEST;
    private final Integer ENTITY_ERROR_CODE;

    public BadRequestException(String message, ErrorEntity entity) {
        super(message);
        this.ENTITY_ERROR_CODE = entity.getCode();
    }

    @Override
    public int getCode() {
        return CODE + this.ENTITY_ERROR_CODE;
    }

    @Override
    public String getMessageCode() {
        return MESSAGE_CODE;
    }

    @Override
    public HttpStatus getStatusCode() {
        return STATUS_CODE;
    }
}
