package ru.vtkachenko.simpletmsback.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
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
    private String name;
    private TestPlanShort testPlan;
    private Long timer;
    private TestRunState state;


    @Data
    @Builder
    public static class TestPlanShort {
        private Long id;
        private String name;
    }

    @Data
    @Builder
    public static class RunTestCase {
        private int orderNumber;
        private Long id;
        private String name;
        private String preconditions;
        @Builder.Default
        private List<RunTestCaseStep> steps = new ArrayList<>();
        private Long timer;
        private TestRunState state;
        private String comment;
    }

    @Data
    @Builder
    public static class RunTestCaseStep {
        private int orderNumber;
        private String name;
        private String action;
        private String expected;
    }
}
