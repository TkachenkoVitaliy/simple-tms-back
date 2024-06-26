package ru.vtkachenko.simpletmsback.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtkachenko.simpletmsback.constant.enums.TestRunCaseState;
import ru.vtkachenko.simpletmsback.constant.enums.TestRunState;
import ru.vtkachenko.simpletmsback.dto.TestPlanDto;
import ru.vtkachenko.simpletmsback.dto.TestRunDto;
import ru.vtkachenko.simpletmsback.dto.request.CreateTestRunDto;
import ru.vtkachenko.simpletmsback.dto.response.PageDto;
import ru.vtkachenko.simpletmsback.exception.business.TestCaseNotFoundException;
import ru.vtkachenko.simpletmsback.exception.business.TestPlanNotFoundException;
import ru.vtkachenko.simpletmsback.exception.business.TestRunCreatingException;
import ru.vtkachenko.simpletmsback.exception.business.TestRunNotFoundException;
import ru.vtkachenko.simpletmsback.mapper.TestRunMapper;
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
    private final TestRunMapper testRunMapper;

    @Transactional
    public TestRun createTestRun(Long projectId, CreateTestRunDto createDto) {
        TestPlanDto testPlan = testPlanService.getTestPlanById(projectId, createDto.getTestPlanId());
        if (testPlan.getTestCases().isEmpty()) {
            String message = String.format("Cant create test run from test plan with id - %s. " +
                    "Because test plan don't have test cases", testPlan.getId());
            log.error(message);
            throw new TestRunCreatingException(message);
        }
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
                    .state(TestRunCaseState.NOT_STARTED)
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
                .currentCaseId(runTestCases.getFirst().getId())
                .build();

        return testRunRepository.save(testRun);
    }

    @Transactional
    public TestRun updateTestRun(Long projectId, TestRunDto testRunDto) {
        TestRun testRun = findTestRunById(projectId, testRunDto.getId());
        testRun.setName(testRunDto.getName());
        testRun.setProjectId(testRunDto.getProjectId());
        testRun.setTimer(testRunDto.getTimer());
        testRun.setCurrentCaseId(testRunDto.getCurrentCaseId());

        TestRunDto.TestPlanShortDto testPlanDto = testRunDto.getTestPlan();
        if (testPlanDto != null) {
            TestRun.TestPlanShort testPlan = new TestRun.TestPlanShort(testPlanDto.getId(), testPlanDto.getName());
            testRun.setTestPlan(testPlan);
        } else {
            testRun.setTestPlan(null);
        }

        List<TestRunDto.RunTestCaseDto> casesDtos = testRunDto.getCases();
        if (casesDtos != null) {
            List<TestRun.RunTestCase> cases = casesDtos.stream().map(runTestCaseDto -> {
                        List<TestRun.RunTestCaseStep> steps = new ArrayList<>();
                        if (runTestCaseDto.getSteps() != null) {
                            steps = runTestCaseDto.getSteps().stream().map(runTestCaseStepDto -> TestRun.RunTestCaseStep.builder()
                                            .id(runTestCaseStepDto.getId())
                                            .orderNumber(runTestCaseStepDto.getOrderNumber())
                                            .name(runTestCaseStepDto.getName())
                                            .action(runTestCaseStepDto.getAction())
                                            .expected(runTestCaseStepDto.getExpected())
                                            .build())
                                    .collect(Collectors.toList());
                        }

                        return TestRun.RunTestCase.builder()
                                .orderNumber(runTestCaseDto.getOrderNumber())
                                .id(runTestCaseDto.getId())
                                .name(runTestCaseDto.getName())
                                .preconditions(runTestCaseDto.getPreconditions())
                                .timer(runTestCaseDto.getTimer())
                                .state(runTestCaseDto.getState())
                                .comment(runTestCaseDto.getComment())
                                .steps(steps)
                                .build();
                    })
                    .collect(Collectors.toList());
            testRun.setCases(cases);
        } else {
            testRun.setCases(new ArrayList<>());
        }

        TestRunState state = testRunDto.getState();
        if (state != null) {
            testRun.setState(state);
        } else {
            testRun.setState(TestRunState.NOT_STARTED);
        }

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

    public void deleteTestRun(Long projectId, String id) {
        TestRun testRun = findTestRunById(projectId, id);
        testRunRepository.delete(testRun);
    }

    public TestRun updateTestRunCase(Long projectId, String testRunId, Long caseId, TestRunDto.RunTestCaseDto caseDto) {
        // TODO добавить проверку на соответствие caseId и caseDto.getId()
        TestRun testRun = findTestRunById(projectId, testRunId);
        // Для установки следующего currentCaseId для testRun'a
        List<TestRun.RunTestCase> uncompletedCases = testRun.getCases().stream()
                .filter(testCase -> !testCase.getState().equals(TestRunCaseState.COMPLETED))
                .toList();

        TestRun.RunTestCase runTestCase = testRun.getCases().stream()
                .filter(testCase -> testCase.getId().equals(caseId))
                .findFirst()
                .orElseThrow(() -> {
                    String message = String.format("Can't update case with - %s. Because testRun with id - %s not contains this case", caseId, testRunId);
                    log.error(message);
                    return new TestCaseNotFoundException(message);
                });

        int uncompletedIndexOfUpdatedCase = uncompletedCases.indexOf(runTestCase);

        runTestCase.setState(caseDto.getState());
        runTestCase.setTimer(caseDto.getTimer());
        runTestCase.setComment(caseDto.getComment());

        if (!caseDto.getState().equals(TestRunCaseState.PAUSED)) {
            if (uncompletedCases.size() <= 1) {
                testRun.setCurrentCaseId(null);
            } else {
                if (uncompletedIndexOfUpdatedCase == uncompletedCases.size() - 1) {
                    testRun.setCurrentCaseId(uncompletedCases.getFirst().getId());
                } else {
                    testRun.setCurrentCaseId(uncompletedCases.get(uncompletedIndexOfUpdatedCase + 1).getId());
                }
            }
        }

        long totalTime = testRun.getCases().stream()
                .mapToLong(TestRun.RunTestCase::getTimer)
                .sum();
        testRun.setTimer(totalTime);
        TestRunState newTestRunState = calcTestRunState(testRun.getCases());
        testRun.setState(newTestRunState);

        return testRunRepository.save(testRun);
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

    private TestRunState calcTestRunState(List<TestRun.RunTestCase> testRunCases) {
        boolean isCompleted = testRunCases.stream()
                .allMatch(runTestCase -> runTestCase.getState().equals(TestRunCaseState.COMPLETED));
        if (isCompleted) {
            return TestRunState.COMPLETED;
        }
        boolean isNotStarted = testRunCases.stream()
                .allMatch(runTestCase -> runTestCase.getState().equals(TestRunCaseState.NOT_STARTED));
        if (isNotStarted) {
            return TestRunState.NOT_STARTED;
        }
        boolean haveFailed = testRunCases.stream()
                .anyMatch(runTestCase -> runTestCase.getState().equals(TestRunCaseState.FAILED));
        if (haveFailed) {
            return TestRunState.FAILED;
        }
        boolean haveBlocked = testRunCases.stream()
                .anyMatch(runTestCase -> runTestCase.getState().equals(TestRunCaseState.BLOCKED));
        if (haveBlocked) {
            return TestRunState.BLOCKED;
        }
        return TestRunState.IN_PROGRESS;
    }
}
