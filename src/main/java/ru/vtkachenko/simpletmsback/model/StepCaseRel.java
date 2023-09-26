package ru.vtkachenko.simpletmsback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class StepCaseRel implements Serializable {
    @EmbeddedId
    private StepCaseId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("testStepId")
    private TestStep testStep;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("testCaseId")
    private TestCase testCase;

    @NotNull
    private Integer orderNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        StepCaseRel that = (StepCaseRel) o;
        return Objects.equals(testStep, that.testStep) &&
                Objects.equals(testCase, that.testCase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testStep, testCase);
    }
}
