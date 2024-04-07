package ru.vtkachenko.simpletmsback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtkachenko.simpletmsback.dto.TestCaseDto;
import ru.vtkachenko.simpletmsback.dto.TestCaseStepDto;
import ru.vtkachenko.simpletmsback.dto.TestStepDto;
import ru.vtkachenko.simpletmsback.exception.business.TestCaseNotFoundException;
import ru.vtkachenko.simpletmsback.mapper.TestCaseMapper;
import ru.vtkachenko.simpletmsback.model.TestCase;
import ru.vtkachenko.simpletmsback.model.TestCaseStep;
import ru.vtkachenko.simpletmsback.model.TestCaseStepId;
import ru.vtkachenko.simpletmsback.model.TestStep;
import ru.vtkachenko.simpletmsback.repository.StepCaseRelRepository;
import ru.vtkachenko.simpletmsback.repository.TestCaseRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public List<TestCase> getTestCasesByIds(List<Long> ids) {
        return testCaseRepository.findAllById(ids);
    }


    public TestCaseDto getTestCase(Long id) {
        TestCase testCase = testCaseRepository.getTestCaseById(id).orElseThrow(() -> {
            String message = String.format("Cant find test case with id - %s", id);
            log.error(message);
            return new TestCaseNotFoundException(message);
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
        // Получаем список id testStep которые получили от клиента и не равны null
        List<Long> testStepsIds = testCaseDto.getSteps().stream()
                .filter(testCaseStepDto -> testCaseStepDto.getTestStep().getId() != null)
                .map(testCaseStepDto -> testCaseStepDto.getTestStep().getId())
                .toList();

        // Получаем шаги тест кейса из dto
        Map<TestCaseStepId, Long> steps = new HashMap<>();
        testCaseDto.getSteps()
                .forEach(step -> steps.put(
                        new TestCaseStepId(step.getTestStep().getId(), testCaseDto.getId(), step.getOrderNumber()),
                        step.getTestStep().getId()
                ));

        // Получаем тест кейс из базы данных с его зависимости через left join
        TestCase testCase = testCaseRepository.getTestCaseById(testCaseDto.getId()).orElseThrow(() -> {
            String message = String.format("Cant update test case with id -%s, cause test case with this id  not found",
                    testCaseDto.getId());
            log.error(message);
            return new TestCaseNotFoundException(message);
        });

        // TODO вынести в отдельный метод маппера updateFromDto
        // Обновляем простые аттрибуты тест кейса
        TestCase newTestCase = mapper.toEntity(testCaseDto);
        testCase.setParentSuite(newTestCase.getParentSuite());
        testCase.setType(newTestCase.getType());
        testCase.setPriority(newTestCase.getPriority());
        testCase.setName(newTestCase.getName());
        testCase.setPreconditions(newTestCase.getPreconditions());
        testCase.setProject(newTestCase.getProject());

        // Получаем список id тех testStep которые потом нужно будет удалить
        List<Long> testStepsToRemoveIds = new ArrayList<>();
        if (testCaseDto.getSteps() != null) {
            testStepsToRemoveIds = testCase.getTestSteps().stream()
                    .map(TestCaseStep::getTestStep)
                    .filter(testStep -> !testStep.getRepeatable())
                    .map(TestStep::getId)
                    .filter(id -> !testStepsIds.contains(id))
                    .collect(Collectors.toList());
        }

        // Удаляем шаги тест кейса которые отсутствуют в dto
        testCase.getTestSteps().removeIf(step -> !steps.containsKey(step.getId()));

        // Удаляем testSteps которые больше не используются в этом кейсе и не являются переиспользуемыми
        if (!testStepsToRemoveIds.isEmpty()) {
            testStepService.deleteAllById(testStepsToRemoveIds);
        }

        List<Integer> persistedOrderNumbers = testCase.getTestSteps().stream()
                .map(step -> step.getId().getOrderNumber())
                .toList();


        // Обновляем предыдущие
        testCaseDto.getSteps().stream()
                .filter(stepDto -> stepDto.getTestStep().getId() != null)
                .forEach(stepDto -> testStepService.saveTestStep(stepDto.getTestStep()));

        // Новые TestCaseSteps
        List<TestCaseStep> newTestCaseSteps = testCaseDto.getSteps().stream()
                .filter(stepDto -> !persistedOrderNumbers.contains(stepDto.getOrderNumber())) // Only new TestSteps
                .map(stepDto -> {
                    TestStep savedTestStep = testStepService.saveTestStep(stepDto.getTestStep());

                    return TestCaseStep.builder()
                            .testStep(savedTestStep)
                            .testCase(testCase)
                            .id(new TestCaseStepId(savedTestStep.getId(), testCase.getId(), stepDto.getOrderNumber()))
                            .build();
                })
                .toList();

        testCase.getTestSteps().addAll(newTestCaseSteps);

        TestCaseDto savedTestCaseDto = mapper.toDto(testCaseRepository.save(testCase));
        // Упорядочиваем testCaseStep по их orderNumber
        savedTestCaseDto.getSteps().sort(Comparator.comparingInt(TestCaseStepDto::getOrderNumber));
        // Возвращаем результат
        return savedTestCaseDto;
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
