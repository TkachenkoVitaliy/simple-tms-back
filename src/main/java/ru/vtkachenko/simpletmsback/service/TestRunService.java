package ru.vtkachenko.simpletmsback.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vtkachenko.simpletmsback.constant.enums.TestRunState;
import ru.vtkachenko.simpletmsback.dto.TestPlanDto;
import ru.vtkachenko.simpletmsback.dto.request.CreateTestRunDto;
import ru.vtkachenko.simpletmsback.model.TestCase;
import ru.vtkachenko.simpletmsback.model.TestCaseStep;
import ru.vtkachenko.simpletmsback.model.TestRun;
import ru.vtkachenko.simpletmsback.model.TestStep;
import ru.vtkachenko.simpletmsback.repository.TestRunRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestRunService {
    private final TestRunRepository testRunRepository;
    private final TestPlanService testPlanService;
    private final TestCaseService testCaseService;

    public TestRun test() {
        TestRun testRun = TestRun.builder()
                .name("testRunFirst")
                .state(TestRunState.NOT_STARTED)
                .build();
        TestRun saved = testRunRepository.save(testRun);
        return saved;
    }

    public TestRun createTestRun(Long projectId, CreateTestRunDto createDto) {
        TestPlanDto testPlan = testPlanService.getTestPlanById(projectId, createDto.getTestPlanId());
        TestRun.TestPlanShort testPlanShort = TestRun.TestPlanShort.builder()
                .id(testPlan.getId())
                .name(testPlan.getName())
                .build();

        List<TestCase> testCases = testCaseService.getTestCasesByIds(new ArrayList<>(testPlan.getTestCases()));
        List<TestRun.RunTestCase> runTestCases = new ArrayList<>();
        // TODO фикс этот странный подход
        final int[] caseOrdinalNumber = {1};
        testCases.forEach((testCase) -> {
            List<TestCaseStep> testSteps = testCase.getTestSteps();
            List<TestRun.RunTestCaseStep> runTestCaseSteps = testSteps.stream().map((testCaseStep) -> {
                TestStep testStep = testCaseStep.getTestStep();
                return TestRun.RunTestCaseStep.builder()
                        .id(testStep.getId())
                        .orderNumber(testCaseStep.getId().getOrderNumber())
                        .name(testStep.getName())
                        .action(testStep.getAction())
                        .expected(testStep.getExpected())
                        .build();
            }).collect(Collectors.toList());

            TestRun.RunTestCase runTestCase = TestRun.RunTestCase.builder()
                    .orderNumber(caseOrdinalNumber[0])
                    .id(testCase.getId())
                    .name(testCase.getName())
                    .preconditions(testCase.getPreconditions())
                    .steps(runTestCaseSteps)
                    .timer(0)
                    .state(TestRunState.NOT_STARTED)
                    .comment("")
                    .build();

            runTestCases.add(runTestCase);
            caseOrdinalNumber[0]++;
        });


        TestRun testRun = TestRun.builder()
                .name(createDto.getName())
                .projectId(projectId)
                .testPlan(testPlanShort)
                .cases(runTestCases)
                .timer(0)
                .state(TestRunState.NOT_STARTED)
                .build();

        return testRunRepository.save(testRun);
    }
}
