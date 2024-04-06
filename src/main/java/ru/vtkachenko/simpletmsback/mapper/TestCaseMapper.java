package ru.vtkachenko.simpletmsback.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.vtkachenko.simpletmsback.dto.TestCaseDto;
import ru.vtkachenko.simpletmsback.dto.TestCaseStepDto;
import ru.vtkachenko.simpletmsback.model.TestCase;
import ru.vtkachenko.simpletmsback.service.ProjectService;
import ru.vtkachenko.simpletmsback.service.TestSuiteService;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TestCaseMapper implements EntityMapper<TestCaseDto, TestCase> {
    private final ProjectService projectService;
    private final TestSuiteService testSuiteService;
    private final TestStepMapper testStepMapper;


    @Override
    public TestCaseDto toDto(TestCase entity) {
        List<TestCaseStepDto> testCaseSteps = entity.getTestSteps().stream()
                .map((stepCaseRel) -> TestCaseStepDto.builder()
                        .orderNumber(stepCaseRel.getId().getOrderNumber())
                        .testStep(testStepMapper.toDto(stepCaseRel.getTestStep()))
                        .build())
                .collect(Collectors.toList());

        return TestCaseDto.builder()
                .id(entity.getId())
                .parentSuiteId(entity.getParentSuite() == null ? null : entity.getParentSuite().getId())
                .name(entity.getName())
                .type(entity.getType())
                .priority(entity.getPriority())
                .preconditions(entity.getPreconditions())
                .steps(testCaseSteps)
                .projectId(entity.getProject().getId())
                .build();
    }

    @Override
    public TestCase toEntity(TestCaseDto dto) {
        return TestCase.builder()
                .id(dto.getId())
                .name(dto.getName())
                .type(dto.getType())
                .priority(dto.getPriority())
                .preconditions(dto.getPreconditions())
                .project(projectService.getProjectReferenceById(dto.getProjectId()))
                .parentSuite(dto.getParentSuiteId() == null ? null : testSuiteService.getTestSuiteReferenceById(dto.getParentSuiteId()))
                .build();
    }
}
