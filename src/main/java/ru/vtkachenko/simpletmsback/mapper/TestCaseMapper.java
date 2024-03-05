package ru.vtkachenko.simpletmsback.mapper;

import org.springframework.stereotype.Component;
import ru.vtkachenko.simpletmsback.dto.TestCaseDto;
import ru.vtkachenko.simpletmsback.dto.TestCaseStepDto;
import ru.vtkachenko.simpletmsback.model.TestCase;
import ru.vtkachenko.simpletmsback.service.ProjectService;
import ru.vtkachenko.simpletmsback.service.TestSuiteService;

import java.util.List;

@Component
public class TestCaseMapper implements EntityMapper<TestCaseDto, TestCase> {
    private final ProjectService projectService;
    private final TestSuiteService testSuiteService;
    private final TestStepMapper testStepMapper;

    public TestCaseMapper(ProjectService projectService, TestSuiteService testSuiteService, TestStepMapper testStepMapper) {
        this.projectService = projectService;
        this.testSuiteService = testSuiteService;
        this.testStepMapper = testStepMapper;
    }

    @Override
    public TestCaseDto toDto(TestCase entity) {
        List<TestCaseStepDto> testCaseSteps = entity.getTestSteps().stream()
                .map((stepCaseRel) -> TestCaseStepDto.builder()
                        .orderNumber(stepCaseRel.getOrderNumber())
                        .testStep(testStepMapper.toDto(stepCaseRel.getTestStep()))
                        .build())
                .toList();

        return TestCaseDto.builder()
                .id(entity.getId())
                .parentSuiteId(entity.getParentSuite() == null ? null : entity.getParentSuite().getId())
                .name(entity.getName())
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
                .preconditions(dto.getPreconditions())
                .project(projectService.getProjectReferenceById(dto.getProjectId()))
                .parentSuite(dto.getParentSuiteId() == null ? testSuiteService.getTestSuiteReferenceById(dto.getParentSuiteId()) : null)
                .build();
    }
}
