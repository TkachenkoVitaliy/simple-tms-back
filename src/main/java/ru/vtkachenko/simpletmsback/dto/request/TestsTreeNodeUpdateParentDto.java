package ru.vtkachenko.simpletmsback.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vtkachenko.simpletmsback.constant.TestsTreeNodeType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestsTreeNodeUpdateParentDto {
    Long nodeId;
    Long parentId;
    TestsTreeNodeType type;
}
