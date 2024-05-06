package ru.vtkachenko.simpletmsback.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTestRunDto {
    @NotNull
    private Long testPlanId;
    @NotNull
    private String name;
}
