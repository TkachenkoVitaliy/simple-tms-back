package ru.vtkachenko.simpletmsback.dto.response;

import lombok.Builder;
import lombok.Data;
import ru.vtkachenko.simpletmsback.dto.TestStepDto;

import java.util.List;

@Data
@Builder
public class TestStepsPageDto {
    private long totalCount;
    private List<TestStepDto> data;
}
