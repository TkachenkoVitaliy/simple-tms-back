package ru.vtkachenko.simpletmsback.exception.enums;

import lombok.Getter;

@Getter
public enum ErrorEntity {
    PROJECT(100_000),
    PLAN(200_000),
    SUITE(300_000),
    CASE(400_000),
    OTHER(900_000);

    private final Integer code;

    ErrorEntity(Integer code) {
        this.code = code;
    }

}
