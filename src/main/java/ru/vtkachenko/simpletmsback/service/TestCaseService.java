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
import ru.vtkachenko.simpletmsback.model.StepCaseRel;
import ru.vtkachenko.simpletmsback.model.TestCase;
import ru.vtkachenko.simpletmsback.repository.TestCaseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestCaseService {
    private final TestCaseRepository testCaseRepository;
    private final TestCaseMapper mapper;
    private final TestStepService testStepService;


    public TestCase getTestCase(Long id) {
        return testCaseRepository.getTestCaseById(id).orElseThrow(() -> {
            String message = String.format("Cant find test case with id - %s", id);
            log.error(message);
            throw new TestCaseNotFoundException(message);
        });
    }

    @Transactional
    public TestCaseDto createTestCase(TestCaseDto testCaseDto) {
        TestCase testCase = mapper.toEntity(testCaseDto);
        TestCase savedTestCase = testCaseRepository.save(testCase);
        List<TestStepDto> testStepDtos = testCaseDto.getSteps().stream()
                .map(TestCaseStepDto::getTestStep)
                .collect(Collectors.toList());
        List<TestStepDto> saveTestSteps = testStepService.saveAllTestSteps(testStepDtos);

//        testCaseDto.getSteps().stream()
//                .map(testCaseStepDto -> {
//                    StepCaseRel.builder()
//                            .orderNumber(testCaseStepDto.getOrderNumber())
//                            .testCase(savedTestCase)
//                            .testStep()
//                            .build();
//                })

        return null;
    }
}
