package ru.vtkachenko.simpletmsback.mapper;

import lombok.experimental.UtilityClass;
import ru.vtkachenko.simpletmsback.dto.ProjectDto;
import ru.vtkachenko.simpletmsback.model.Project;

@UtilityClass
public class ProjectMapper {
    public static ProjectDto toDto(Project entity) {
        return ProjectDto.builder()
                .id(entity.getId())
                .name(entity.getName() != null ? entity.getName() : "")
                .description(entity.getDescription() != null ? entity.getDescription() : "")
                .build();
    }

    public static Project toEntity(ProjectDto dto) {
        return Project.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

}
