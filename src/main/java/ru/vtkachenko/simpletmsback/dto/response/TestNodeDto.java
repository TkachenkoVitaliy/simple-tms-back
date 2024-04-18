package ru.vtkachenko.simpletmsback.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vtkachenko.simpletmsback.constant.enums.TestsTreeNodeType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestNodeDto {
    private Long id;
    private Long parentSuiteId;
    private String name;
    private TestsTreeNodeType type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TestNodeDto> children;
}
