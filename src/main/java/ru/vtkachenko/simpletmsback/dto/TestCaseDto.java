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
    @NotNull
    private String name;
    private String preconditions;
    @NotNull
    private Long projectId;
    private Long parentSuiteId;
    @Builder.Default
    private List<TestStepDto> testSteps = new ArrayList<>();
}
