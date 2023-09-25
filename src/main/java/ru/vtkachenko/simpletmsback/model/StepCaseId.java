package ru.vtkachenko.simpletmsback.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class StepCaseId implements Serializable {
    @Column(name="test_step_id")
    private Long testStepId;
    @Column(name="test_case_id")
    private Long testCaseId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        StepCaseId that = (StepCaseId) o;
        return Objects.equals(testStepId, that.testStepId) &&
                Objects.equals(testCaseId, that.testCaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testStepId, testCaseId);
    }
}
