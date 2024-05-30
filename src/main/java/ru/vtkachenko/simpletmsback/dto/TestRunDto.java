package ru.vtkachenko.simpletmsback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vtkachenko.simpletmsback.constant.enums.TestRunCaseState;
import ru.vtkachenko.simpletmsback.constant.enums.TestRunState;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class TestRunDto {
    private String id;
    @NotNull
    private String name;
    @NotNull
    private Long projectId;
    @NotNull
    private TestPlanShortDto testPlan;
    @Builder.Default
    private List<RunTestCaseDto> cases = new ArrayList<>();
    private long timer;
    @Builder.Default
    private TestRunState state = TestRunState.NOT_STARTED;
    private Long currentCaseId;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TestPlanShortDto {
        private Long id;
        private String name;
    }

    @Data
    @Builder
    public static class RunTestCaseDto {
        private int orderNumber;
        @NotNull
        private Long id;
        @NotNull
        private String name;
        private String preconditions;
        @Builder.Default
        private List<RunTestCaseStepDto> steps = new ArrayList<>();
        private long timer;
        @Builder.Default
        private TestRunCaseState state = TestRunCaseState.NOT_STARTED;
        private String comment;
    }

    @Data
    @Builder
    public static class RunTestCaseStepDto {
        @NotNull
        private Long id;
        @NotNull
        private Integer orderNumber;
        private String name;
        @NotNull
        private String action;
        private String expected;
    }
}
