package ru.vtkachenko.simpletmsback.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.vtkachenko.simpletmsback.model.enums.CasePriority;
import ru.vtkachenko.simpletmsback.model.enums.CaseType;

@Data
@Builder
public class TestCaseShortDto {
    private Long id;
    private Long parentSuiteId;
    @NotNull
    private String name;
    @NotNull
    private CaseType type;
    @NotNull
    private CasePriority priority;
    @NotNull
    private Long projectId;
}
