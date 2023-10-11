package ru.vtkachenko.simpletmsback.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestSuiteShortDto {
    private Long id;
    private String name;
}
