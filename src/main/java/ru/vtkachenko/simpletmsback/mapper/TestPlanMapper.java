package ru.vtkachenko.simpletmsback.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.vtkachenko.simpletmsback.dto.TestPlanDto;
import ru.vtkachenko.simpletmsback.dto.response.TestCaseShortDto;
import ru.vtkachenko.simpletmsback.model.TestPlan;
import ru.vtkachenko.simpletmsback.service.ProjectService;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TestPlanMapper implements EntityMapper<TestPlanDto, TestPlan> {
    private final ProjectService projectService;
    private final TestCaseShortMapper testCaseShortMapper;

    @Override
    public TestPlanDto toDto(TestPlan entity) {
        Set<TestCaseShortDto> testCasesShortDto = entity.getTestCases().stream()
                .map(testCaseShortMapper::toDto)
                .collect(Collectors.toSet());

        return TestPlanDto.builder()
                .id(entity.getId())
                .projectId(entity.getProject().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .testCases(testCasesShortDto)
                .build();
    }

    @Override
    public TestPlan toEntity(TestPlanDto dto) {
        return TestPlan.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .project(projectService.getProjectReferenceById(dto.getProjectId()))
                .build();
    }
}
