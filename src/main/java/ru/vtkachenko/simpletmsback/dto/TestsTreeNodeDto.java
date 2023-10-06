package ru.vtkachenko.simpletmsback.dto;

import lombok.Builder;
import lombok.Data;
import ru.vtkachenko.simpletmsback.constant.TestsTreeNodeType;

import java.util.List;

@Data
public class TestsTreeNodeDto {
    private long id;
    private String text;
    private long parent;
    private boolean droppable;

    private TestsTreeNodeDataDto data;

    @Builder
    public TestsTreeNodeDto(long id, String text, long parent, List<Long> children, TestsTreeNodeType type) {
        this.id = id;
        this.text = text;
        this.parent = parent;
        this.droppable = type == TestsTreeNodeType.SUITE;
        this.data = TestsTreeNodeDataDto.builder()
                .children(children)
                .type(type)
                .build();
    }
}
