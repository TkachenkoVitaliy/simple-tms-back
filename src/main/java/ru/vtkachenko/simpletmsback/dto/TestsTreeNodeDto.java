package ru.vtkachenko.simpletmsback.dto;

import lombok.Builder;
import lombok.Data;
import ru.vtkachenko.simpletmsback.constant.TestsTreeNodeType;

import java.util.List;

@Data
public class TestsTreeNodeDto {
    private String id;
    private String text;
    private String parent;
    private boolean droppable;

    private TestsTreeNodeDataDto data;

    @Builder
    public TestsTreeNodeDto(long id, String text, Long parentId, List<String> children, TestsTreeNodeType type) {
        this.id = type.toString().toLowerCase() + "/" + id;
        this.text = text;
        this.parent = parentId == null ? "0" : "suite/" + parentId;
        this.droppable = type == TestsTreeNodeType.SUITE;
        this.data = TestsTreeNodeDataDto.builder()
                .id(id)
                .children(children)
                .type(type)
                .parentId(parentId)
                .build();
    }
}
