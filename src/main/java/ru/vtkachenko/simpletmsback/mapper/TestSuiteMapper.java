package ru.vtkachenko.simpletmsback.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.vtkachenko.simpletmsback.dto.TestSuiteDto;
import ru.vtkachenko.simpletmsback.model.TestSuite;
import ru.vtkachenko.simpletmsback.service.ProjectService;
import ru.vtkachenko.simpletmsback.service.TestSuiteService;

@Component
@RequiredArgsConstructor
public class TestSuiteMapper implements EntityMapper<TestSuiteDto, TestSuite> {
    private final ProjectService projectService;
    private final TestSuiteService testSuiteService;

    @Override
    public TestSuiteDto toDto(TestSuite entity) {
        return TestSuiteDto.builder()
                .id(entity.getId())
                .projectId(entity.getProject().getId())
                .parentSuiteId(entity.getParentSuite() == null ? null : entity.getParentSuite().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    @Override
    public TestSuite toEntity(TestSuiteDto dto) {
        return TestSuite.builder()
                .id(dto.getProjectId())
                .project(projectService.getProjectReferenceById(dto.getProjectId()))
                .parentSuite(testSuiteService.getTestSuiteReferenceById(dto.getParentSuiteId()))
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }
}
