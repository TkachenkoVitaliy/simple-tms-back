package ru.vtkachenko.simpletmsback.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class PageDto<T> {
    private long totalCount;
    private List<T> data;
}
