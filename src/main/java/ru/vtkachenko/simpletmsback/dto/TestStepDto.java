package ru.vtkachenko.simpletmsback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestStepDto {
    private Long id;
    private String name;
    @NotNull
    private Boolean repeatable;
    @NotNull
    private String action;
    private String expected;
    @NotNull
    private Long projectId;
}
