package ru.vtkachenko.simpletmsback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.vtkachenko.simpletmsback.dto.response.TestCaseShortDto;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class TestPlanDto {
    private Long id;
    @NotNull
    private Long projectId;
    @NotNull
    private String name;
    private String description;
    @Builder.Default
    private Set<TestCaseShortDto> testCases = new HashSet<>();
}
