package ru.vtkachenko.simpletmsback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorInfo {
    private int code;
    private String message;
}
