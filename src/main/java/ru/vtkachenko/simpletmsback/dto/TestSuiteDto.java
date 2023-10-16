package ru.vtkachenko.simpletmsback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestSuiteDto {
    private Long id;
    @NotNull
    private Long projectId;
    private Long parentSuiteId;
    private String name;
    private String description;
}
