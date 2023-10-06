package ru.vtkachenko.simpletmsback.dto;

import lombok.Builder;
import lombok.Data;
import ru.vtkachenko.simpletmsback.constant.TestsTreeNodeType;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class TestsTreeNodeDataDto {
    @Builder.Default
    List<Long> children = new ArrayList<>();
    TestsTreeNodeType type;
}
