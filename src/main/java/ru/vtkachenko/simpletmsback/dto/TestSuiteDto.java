package ru.vtkachenko.simpletmsback.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestSuiteDto {
    private Long id;
    private Long projectId;
    private Long parentSuiteId;
    private String name;
    private String description;
}
