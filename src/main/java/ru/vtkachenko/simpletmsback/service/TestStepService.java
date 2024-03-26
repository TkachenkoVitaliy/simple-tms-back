package ru.vtkachenko.simpletmsback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.vtkachenko.simpletmsback.dto.TestStepDto;
import ru.vtkachenko.simpletmsback.dto.response.TestStepsPageDto;
import ru.vtkachenko.simpletmsback.mapper.TestStepMapper;
import ru.vtkachenko.simpletmsback.model.TestStep;
import ru.vtkachenko.simpletmsback.repository.TestStepRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestStepService {

    private final TestStepRepository testStepRepository;
    private final TestStepMapper mapper;

    public TestStep saveTestStep(TestStepDto testStepDto) {
        return testStepRepository.save(mapper.toEntity(testStepDto));
    }

    public List<TestStepDto> saveAllTestSteps(List<TestStepDto> testStepDtos) {
        List<TestStep> testSteps = testStepDtos.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList());
        List<TestStep> savedTestSteps = testStepRepository.saveAll(testSteps);
        return savedTestSteps.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public TestStepsPageDto getRepeatableSteps(Integer offset, Integer limit) {
        Page<TestStep> testStepsPage = testStepRepository.findAllByRepeatableIsTrue(PageRequest.of(offset, limit));
        List<TestStepDto> testSteps = testStepsPage.getContent().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return TestStepsPageDto.builder()
                .totalCount(testStepsPage.getTotalElements())
                .data(testSteps)
                .build();
    }

    public void deleteAllById(List<Long> ids) {
        testStepRepository.deleteAllById(ids);
    }
}
