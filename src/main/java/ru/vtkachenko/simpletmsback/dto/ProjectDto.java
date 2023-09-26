package ru.vtkachenko.simpletmsback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class ProjectDto {
    private Long id;
    @NotNull
    @Builder.Default
    private String name = "";
    @NotNull
    @Builder.Default
    private String description = "";
}
