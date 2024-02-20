package ru.vtkachenko.simpletmsback.mapper;

import org.springframework.stereotype.Component;
import ru.vtkachenko.simpletmsback.dto.TestStepDto;
import ru.vtkachenko.simpletmsback.model.TestStep;
import ru.vtkachenko.simpletmsback.service.ProjectService;

@Component
public class TestStepMapper implements EntityMapper<TestStepDto, TestStep> {
    private final ProjectService projectService;

    public TestStepMapper(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public TestStepDto toDto(TestStep entity) {
        return TestStepDto.builder()
                .id(entity.getId())
                .projectId(entity.getProject().getId())
                .name(entity.getName())
                .repeatable(entity.getRepeatable())
                .action(entity.getAction())
                .expected(entity.getExpected())
                .build();
    }

    @Override
    public TestStep toEntity(TestStepDto dto) {
        return TestStep.builder()
                .id(dto.getId())
                .project(projectService.getProjectReferenceById(dto.getProjectId()))
                .name(dto.getName())
                .repeatable(dto.getRepeatable())
                .action(dto.getAction())
                .expected(dto.getExpected())
                .build();
    }
}
