package ru.vtkachenko.simpletmsback.dto;

import lombok.Builder;
import lombok.Data;
import ru.vtkachenko.simpletmsback.constant.enums.TestsTreeNodeType;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class TestsTreeNodeDataDto {
    Long id;
    @Builder.Default
    List<String> children = new ArrayList<>();
    TestsTreeNodeType type;
    Long parentId;
}
