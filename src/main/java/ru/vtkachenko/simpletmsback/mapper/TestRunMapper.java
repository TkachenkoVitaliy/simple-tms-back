package ru.vtkachenko.simpletmsback.mapper;

import org.springframework.stereotype.Component;
import ru.vtkachenko.simpletmsback.dto.TestRunDto;
import ru.vtkachenko.simpletmsback.model.TestRun;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestRunMapper implements EntityMapper<TestRunDto, TestRun> {
    @Override
    public TestRunDto toDto(TestRun entity) {
        TestRunDto.TestPlanShortDto testPlan = new TestRunDto.TestPlanShortDto(
                entity.getTestPlan().getId(),
                entity.getTestPlan().getName()
        );

        List<TestRunDto.RunTestCaseDto> cases = entity.getCases().stream().map(runTestCase -> {
            List<TestRunDto.RunTestCaseStepDto> steps = runTestCase.getSteps().stream()
                    .map(runTestCaseStep -> TestRunDto.RunTestCaseStepDto.builder()
                            .id(runTestCaseStep.getId())
                            .orderNumber(runTestCaseStep.getOrderNumber())
                            .name(runTestCaseStep.getName())
                            .action(runTestCaseStep.getAction())
                            .expected(runTestCaseStep.getExpected())
                            .build())
                    .collect(Collectors.toList());

            return TestRunDto.RunTestCaseDto.builder()
                    .orderNumber(runTestCase.getOrderNumber())
                    .id(runTestCase.getId())
                    .name(runTestCase.getName())
                    .preconditions(runTestCase.getPreconditions())
                    .steps(steps)
                    .timer(runTestCase.getTimer())
                    .state(runTestCase.getState())
                    .comment(runTestCase.getComment())
                    .build();
        }).collect(Collectors.toList());

        return TestRunDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .projectId(entity.getProjectId())
                .testPlan(testPlan)
                .cases(cases)
                .timer(entity.getTimer())
                .state(entity.getState())
                .currentCaseId(entity.getCurrentCaseId())
                .build();
    }

    @Override
    public TestRun toEntity(TestRunDto dto) {
        TestRun.TestPlanShort testPlanShort = new TestRun.TestPlanShort(
                dto.getTestPlan().getId(),
                dto.getTestPlan().getName()
        );

        List<TestRun.RunTestCase> cases = dto.getCases().stream().map(runTestCase -> {
            List<TestRun.RunTestCaseStep> steps = runTestCase.getSteps().stream()
                    .map(runTestCaseStep -> TestRun.RunTestCaseStep.builder()
                            .id(runTestCaseStep.getId())
                            .orderNumber(runTestCaseStep.getOrderNumber())
                            .name(runTestCaseStep.getName())
                            .action(runTestCaseStep.getAction())
                            .expected(runTestCaseStep.getExpected())
                            .build())
                    .collect(Collectors.toList());

            return TestRun.RunTestCase.builder()
                    .orderNumber(runTestCase.getOrderNumber())
                    .id(runTestCase.getId())
                    .name(runTestCase.getName())
                    .preconditions(runTestCase.getPreconditions())
                    .steps(steps)
                    .timer(runTestCase.getTimer())
                    .state(runTestCase.getState())
                    .comment(runTestCase.getComment())
                    .build();
        }).collect(Collectors.toList());

        return TestRun.builder()
                .id(dto.getId())
                .name(dto.getName())
                .projectId(dto.getProjectId())
                .testPlan(testPlanShort)
                .cases(cases)
                .timer(dto.getTimer())
                .state(dto.getState())
                .currentCaseId(dto.getCurrentCaseId())
                .build();
    }
}
