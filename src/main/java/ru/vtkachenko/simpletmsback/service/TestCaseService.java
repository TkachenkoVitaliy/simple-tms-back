package ru.vtkachenko.simpletmsback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtkachenko.simpletmsback.dto.TestCaseDto;
import ru.vtkachenko.simpletmsback.dto.TestStepDto;
import ru.vtkachenko.simpletmsback.exception.business.TestCaseNotFoundException;
import ru.vtkachenko.simpletmsback.mapper.TestCaseMapper;
import ru.vtkachenko.simpletmsback.model.StepCaseId;
import ru.vtkachenko.simpletmsback.model.StepCaseRel;
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

        List<StepCaseRel> stepsCaseRel = testCaseDto.getSteps().stream()
                .map(testCaseStepDto -> {
                    TestStepDto testStepDto = testCaseStepDto.getTestStep();
                    TestStep savedTestStep = testStepService.saveTestStep(testStepDto);

                    StepCaseRel stepCaseRel = StepCaseRel.builder()
                            .testStep(savedTestStep)
                            .testCase(savedTestCase)
                            .id(new StepCaseId(savedTestStep.getId(), savedTestCase.getId()))
                            .orderNumber(testCaseStepDto.getOrderNumber())
                            .build();

                    return stepCaseRel;
                }).collect(Collectors.toList());
        List<StepCaseRel> savedStepsCaseRel = stepCaseRelRepository.saveAll(stepsCaseRel);

        savedTestCase.setTestSteps(savedStepsCaseRel);

        return mapper.toDto(savedTestCase);
    }
}
