package ru.vtkachenko.simpletmsback.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.vtkachenko.simpletmsback.constant.enums.TestRunState;
import ru.vtkachenko.simpletmsback.dto.TestPlanDto;
import ru.vtkachenko.simpletmsback.dto.request.CreateTestRunDto;
import ru.vtkachenko.simpletmsback.dto.response.PageDto;
import ru.vtkachenko.simpletmsback.exception.business.TestPlanNotFoundException;
import ru.vtkachenko.simpletmsback.exception.business.TestRunNotFoundException;
import ru.vtkachenko.simpletmsback.model.TestCase;
import ru.vtkachenko.simpletmsback.model.TestCaseStep;
import ru.vtkachenko.simpletmsback.model.TestRun;
import ru.vtkachenko.simpletmsback.model.TestStep;
import ru.vtkachenko.simpletmsback.repository.TestRunRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestRunService {
    private final TestRunRepository testRunRepository;
    private final TestPlanService testPlanService;
    private final TestCaseService testCaseService;

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

    public PageDto<TestRun> getTestRunsPageable(Long projectId, Integer page, Integer pageSize) {
        Page<TestRun> runsPageByProjectId = testRunRepository.findAllByProjectId(projectId, PageRequest.of(page, pageSize));
        return new PageDto<>(runsPageByProjectId.getTotalElements(), runsPageByProjectId.getContent());
    }

    public TestRun getTestRun(Long projectId, String testRunId) {
        TestRun testRun = findTestRunById(projectId, testRunId);
        return testRun;
    }

    private TestRun findTestRunById(Long projectId, String id) {
        TestRun testRun = testRunRepository.findById(id).orElseThrow(() -> {
            String message = String.format("Cant find test run with id - %s", id);
            log.error(message);
            return new TestRunNotFoundException(message);
        });
        if (!Objects.equals(testRun.getProjectId(), projectId)) {
            String message = String.format("Cant find test run with id - %s in project with id -- %s",
                    id, projectId);
            log.error(message);
            throw new TestPlanNotFoundException(message);
        }
        return testRun;
    }
}
