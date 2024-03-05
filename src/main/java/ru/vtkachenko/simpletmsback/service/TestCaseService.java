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

import java.util.List;
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

        // Обновляем простые аттрибуты тест кейса
        TestCase newTestCase = mapper.toEntity(testCaseDto);
        testCase.setParentSuite(newTestCase.getParentSuite());
        testCase.setName(newTestCase.getName());
        testCase.setPreconditions(newTestCase.getPreconditions());
        testCase.setProject(newTestCase.getProject());

        // Получаем список id тех testStep которые необходимо удалить т.к. они не переиспользуемые и больше не привязаны
        // к тест кейсу который мы обновляем
        List<Long> orphanNonRepeatableStepIds = testCase.getTestSteps().stream()
                .map(TestCaseStep::getTestStep)
                .filter(testStep -> !testStep.getRepeatable())
                .map(TestStep::getId)
                .filter(id -> !testStepsIds.contains(id))
                .collect(Collectors.toList());
        // Удаляем все старые testCaseSteps
        testCase.removeAllTestSteps();
        // Удаляем осиротевшие непереиспользуемые testSteps
        testStepService.deleteAllById(orphanNonRepeatableStepIds);

        // Создаем новые testCaseSteps
        List<TestCaseStep> testCaseSteps = testCaseDto.getSteps().stream()
                .map(testCaseStepDto -> {
                    TestStepDto testStepDto = testCaseStepDto.getTestStep();
                    TestStep savedTestStep = testStepService.saveTestStep(testStepDto);

                    return TestCaseStep.builder()
                            .testStep(savedTestStep)
                            .testCase(testCase)
                            .id(new TestCaseStepId(savedTestStep.getId(), testCase.getId(), testCaseStepDto.getOrderNumber()))
                            .build();
                }).collect(Collectors.toList());
        // Добавляем новые созданные testCaseSteps в entity TestCase
        testCase.getTestSteps().addAll(testCaseSteps);
        // Возвращаем результат
        return mapper.toDto(testCaseRepository.save(testCase));/*testCaseWithoutSteps));*/
    }
}
