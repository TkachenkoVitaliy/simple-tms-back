package ru.vtkachenko.simpletmsback.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "testRuns")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TestRun {
    @Id
    private Long id;
    private String name;
}
