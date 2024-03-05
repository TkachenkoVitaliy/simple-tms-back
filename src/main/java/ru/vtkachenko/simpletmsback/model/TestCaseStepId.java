package ru.vtkachenko.simpletmsback.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class TestCaseStepId implements Serializable {
    @Column(name = "test_step_id")
    private Long testStepId;
    @Column(name = "test_case_id")
    private Long testCaseId;
    @Column(name="order_number")
    private Integer orderNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        TestCaseStepId that = (TestCaseStepId) o;
        return Objects.equals(testStepId, that.testStepId) &&
                Objects.equals(testCaseId, that.testCaseId) &&
                Objects.equals(orderNumber, that.orderNumber);
    }

    @Override
    public String toString() {
        return "TestCaseStepId{" +
                "testStepId=" + testStepId +
                ", testCaseId=" + testCaseId +
                ", orderNumber=" + orderNumber +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(testStepId, testCaseId, orderNumber);
    }
}
