package ru.vtkachenko.simpletmsback.exception.business;

import org.springframework.http.HttpStatus;
import ru.vtkachenko.simpletmsback.exception.enums.ErrorEntity;

public class TestSuiteNotFoundException extends BusinessException {
    private static final int CODE = 404 + ErrorEntity.SUITE.getCode();
    private static final String MESSAGE_CODE = "not_found_test_suite";
    private static final HttpStatus STATUS_CODE = HttpStatus.NOT_FOUND;

    public TestSuiteNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getCode() {
        return CODE;
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
