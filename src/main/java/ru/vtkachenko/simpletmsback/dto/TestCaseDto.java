package ru.vtkachenko.simpletmsback.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.vtkachenko.simpletmsback.constant.enums.CasePriority;
import ru.vtkachenko.simpletmsback.constant.enums.CaseType;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class TestCaseDto {
    private Long id;
    private Long parentSuiteId;
    @NotNull
    private String name;
    @NotNull
    private CaseType type;
    @NotNull
    private CasePriority priority;
    private String preconditions;
    @JsonProperty("testSteps")
    @Builder.Default
    private List<TestCaseStepDto> steps = new ArrayList<>();
    @NotNull
    private Long projectId;
}
