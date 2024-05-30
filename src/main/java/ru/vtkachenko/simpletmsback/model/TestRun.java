package ru.vtkachenko.simpletmsback.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.HashIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.vtkachenko.simpletmsback.constant.enums.TestRunCaseState;
import ru.vtkachenko.simpletmsback.constant.enums.TestRunState;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "testRuns")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TestRun {
    @Id
    private String id;
    @NotNull
    private String name;
    @NotNull
    @Indexed
    @HashIndexed
    private Long projectId;
    @NotNull
    private TestPlanShort testPlan;
    @Builder.Default
    private List<RunTestCase> cases = new ArrayList<>();
    private long timer;
    @Builder.Default
    private TestRunState state = TestRunState.NOT_STARTED;
    private Long currentCaseId;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TestPlanShort {
        private Long id;
        private String name;
    }

    @Data
    @Builder
    public static class RunTestCase {
        private int orderNumber;
        @NotNull
        private Long id;
        @NotNull
        private String name;
        private String preconditions;
        @Builder.Default
        private List<RunTestCaseStep> steps = new ArrayList<>();
        private long timer;
        @Builder.Default
        private TestRunCaseState state = TestRunCaseState.NOT_STARTED;
        private String comment;
    }

    @Data
    @Builder
    public static class RunTestCaseStep {
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
