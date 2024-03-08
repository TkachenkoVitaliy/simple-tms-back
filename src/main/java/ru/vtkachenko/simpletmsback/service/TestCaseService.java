package ru.vtkachenko.simpletmsback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtkachenko.simpletmsback.dto.TestCaseDto;
import ru.vtkachenko.simpletmsback.dto.TestStepDto;
import ru.vtkachenko.simpletmsback.exception.business.TestCaseNotFoundException;
import ru.vtkachenko.simpletmsback.mapper.TestCaseMapper;
import ru.vtkachenko.simpletmsback.model.TestCaseStepId;
import ru.vtkachenko.simpletmsback.model.TestCaseStep;
import ru.vtkachenko.simpletmsback.model.TestCase;
import ru.vtkachenko.simpletmsback.model.TestStep;
import ru.vtkachenko.simpletmsback.repository.StepCaseRelRepository;
import ru.vtkachenko.simpletmsback.repository.TestCaseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestCaseService {
    private final TestCaseRepository testCaseRepository;
    private final StepCaseRelRepository stepCaseRelRepository;
    private final TestCaseMapper mapper;
    private final TestStepService testStepService;


    public TestCaseDto getTestCase(Long id) {
        TestCase testCase = testCaseRepository.getTestCaseById(id).orElseThrow(() -> {
            String message = String.format("Cant find test case with id - %s", id);
            log.error(message);
            throw new TestCaseNotFoundException(message);
        });
        return mapper.toDto(testCase);
    }

    @Transactional
    public TestCaseDto createTestCase(TestCaseDto testCaseDto) {
        TestCase testCase = mapper.toEntity(testCaseDto);
        TestCase savedTestCase = testCaseRepository.save(testCase);

        List<TestCaseStep> stepsCaseRel = testCaseDto.getSteps().stream()
                .map(testCaseStepDto -> {
                    TestStepDto testStepDto = testCaseStepDto.getTestStep();
                    TestStep savedTestStep = testStepService.saveTestStep(testStepDto);

                    return TestCaseStep.builder()
                            .testStep(savedTestStep)
                            .testCase(savedTestCase)
                            .id(new TestCaseStepId(savedTestStep.getId(), savedTestCase.getId(), testCaseStepDto.getOrderNumber()))
                            .build();
                }).collect(Collectors.toList());
        List<TestCaseStep> savedStepsCaseRel = stepCaseRelRepository.saveAll(stepsCaseRel);

        savedTestCase.getTestSteps().addAll(savedStepsCaseRel);

        return mapper.toDto(savedTestCase);
    }

    @Transactional
    public TestCaseDto updateTestCase(TestCaseDto testCaseDto) {
        // Получаем список id testStep которые получили от клиента
        List<Long> testStepsIds = testCaseDto.getSteps().stream()
                .map(testCaseStepDto -> testCaseStepDto.getTestStep().getId())
                .toList();

        // Получаем тест кейс с его зависимости через left join
        TestCase testCase = testCaseRepository.getTestCaseById(testCaseDto.getId()).orElseThrow(() -> {
            String message = String.format("Cant update test case with id -%s, cause test case with this id  not found",
                    testCaseDto.getId());
            log.error(message);
            throw new TestCaseNotFoundException(message);
        });

        // TODO вынести в отдельный метод маппера updateFromDto
        // Обновляем простые аттрибуты тест кейса
        TestCase newTestCase = mapper.toEntity(testCaseDto);
        testCase.setParentSuite(newTestCase.getParentSuite());
        testCase.setName(newTestCase.getName());
        testCase.setPreconditions(newTestCase.getPreconditions());
        testCase.setProject(newTestCase.getProject());

        // Получаем список id тех testStep которые необходимо удалить
        List<Long> testStepsToRemoveIds = new ArrayList<>();
        if (testCaseDto.getSteps() != null) {
            testStepsToRemoveIds = testCase.getTestSteps().stream()
                    .map(TestCaseStep::getTestStep)
                    .map(TestStep::getId)
                    .filter(id -> testCaseDto.getSteps().stream()
                            .filter(stepDto -> stepDto.getTestStep() != null && stepDto.getTestStep().getId() != null)
                            .noneMatch(stepDto -> stepDto.getTestStep().getId().equals(id)))
                    .collect(Collectors.toList());
        }

        List<Long> removableIds = testStepsToRemoveIds;

        // Удаляем лишние TestCaseSteps
        testCase.getTestSteps().removeIf(step -> removableIds.contains(step.getTestStep().getId()));

        // Новые TestCaseSteps
        List<TestCaseStep> newTestCaseSteps = testCaseDto.getSteps().stream()
                .filter(stepDto -> stepDto.getTestStep().getId() == null) // Only new TestSteps
                .map(stepDto -> {
                    TestStep savedTestStep = testStepService.saveTestStep(stepDto.getTestStep());

                    return TestCaseStep.builder()
                            .testStep(savedTestStep)
                            .testCase(testCase)
                            .id(new TestCaseStepId(savedTestStep.getId(), testCase.getId(), stepDto.getOrderNumber()))
                            .build();
                })
                .collect(Collectors.toList());

        testCase.getTestSteps().addAll(newTestCaseSteps);

        // Возвращаем результат
        return mapper.toDto(testCaseRepository.save(testCase));/*testCaseWithoutSteps));*/
    }

    @Transactional
    public void deleteTestCase(Long id) {
        Optional<TestCase> testCaseById = testCaseRepository.getTestCaseById(id);
        if (testCaseById.isPresent()) {
            TestCase testCase = testCaseById.get();
            List<Long> testStepIdsForDelete = testCase.getTestSteps().stream()
                    .filter(testCaseStep -> !testCaseStep.getTestStep().getRepeatable())
                    .map(testCaseStep -> testCaseStep.getTestStep().getId())
                    .collect(Collectors.toList());
            testCaseRepository.delete(testCase);
            testStepService.deleteAllById(testStepIdsForDelete);
        }
    }
}
