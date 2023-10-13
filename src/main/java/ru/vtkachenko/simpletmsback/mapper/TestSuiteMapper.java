package ru.vtkachenko.simpletmsback.mapper;

import org.springframework.stereotype.Component;
import ru.vtkachenko.simpletmsback.dto.TestSuiteDto;
import ru.vtkachenko.simpletmsback.model.Project;
import ru.vtkachenko.simpletmsback.model.TestSuite;
import ru.vtkachenko.simpletmsback.service.ProjectService;
import ru.vtkachenko.simpletmsback.service.TestSuiteService;

@Component
public class TestSuiteMapper implements EntityMapper<TestSuiteDto, TestSuite> {
    private final ProjectService projectService;
    private final TestSuiteService testSuiteService;

    public TestSuiteMapper(ProjectService projectService, TestSuiteService testSuiteService) {
        this.projectService = projectService;
        this.testSuiteService = testSuiteService;
    }

    public TestSuiteDto toDto(TestSuite entity) {
        return TestSuiteDto.builder()
                .id(entity.getId())
                .projectId(entity.getProject().getId())
                .parentSuiteId(entity.getParentSuite() == null ? null : entity.getParentSuite().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    public TestSuite toEntity(TestSuiteDto dto) {
        return TestSuite.builder()
                .id(null)
                .project(projectService.getProjectReferenceById(dto.getProjectId()))
                .parentSuite(testSuiteService.getTestSuiteReferenceById(dto.getParentSuiteId()))
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }
}
