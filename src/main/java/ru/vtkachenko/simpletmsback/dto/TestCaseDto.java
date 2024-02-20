package ru.vtkachenko.simpletmsback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class TestCaseDto {
    private Long id;
    private Long parentSuiteId;
    @NotNull
    private String name;
    private String preconditions;
    @Builder.Default
    private List<TestCaseStepDto> steps = new ArrayList<>();
    @NotNull
    private Long projectId;
}
