package ru.vtkachenko.simpletmsback.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.vtkachenko.simpletmsback.dto.response.TestCaseShortDto;
import ru.vtkachenko.simpletmsback.model.TestCase;
import ru.vtkachenko.simpletmsback.service.ProjectService;
import ru.vtkachenko.simpletmsback.service.TestSuiteService;

@Component
@RequiredArgsConstructor
public class TestCaseShortMapper implements EntityMapper<TestCaseShortDto, TestCase> {
    private final ProjectService projectService;
    private final TestSuiteService testSuiteService;

    @Override
    public TestCaseShortDto toDto(TestCase entity) {
        return TestCaseShortDto.builder()
                .id(entity.getId())
                .parentSuiteId(entity.getParentSuite() == null ? null : entity.getParentSuite().getId())
                .name(entity.getName())
                .type(entity.getType())
                .priority(entity.getPriority())
                .projectId(entity.getProject().getId())
                .build();
    }

    @Override
    public TestCase toEntity(TestCaseShortDto dto) {
        return TestCase.builder()
                .id(dto.getId())
                .name(dto.getName())
                .type(dto.getType())
                .priority(dto.getPriority())
                .project(projectService.getProjectReferenceById(dto.getProjectId()))
                .parentSuite(dto.getParentSuiteId() == null ? null : testSuiteService.getTestSuiteReferenceById(dto.getParentSuiteId()))
                .build();
    }
}
