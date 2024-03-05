package ru.vtkachenko.simpletmsback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "steps_cases_rel")
public class TestCaseStep implements Serializable {
    @JsonIgnore
    @EmbeddedId
    private TestCaseStepId id;
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("testStepId")
    private TestStep testStep;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("testCaseId")
    private TestCase testCase;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        TestCaseStep that = (TestCaseStep) o;
        return Objects.equals(testStep, that.testStep) &&
                Objects.equals(testCase, that.testCase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testStep, testCase);
    }
}
