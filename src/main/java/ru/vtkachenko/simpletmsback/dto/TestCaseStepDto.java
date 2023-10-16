package ru.vtkachenko.simpletmsback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestCaseStepDto {
    @NotNull
    private Integer orderNumber;
    private TestStepDto testStep;
}
