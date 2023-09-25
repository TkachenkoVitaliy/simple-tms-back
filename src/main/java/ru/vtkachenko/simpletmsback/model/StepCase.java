package ru.vtkachenko.simpletmsback.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "steps_cases")
public class StepCase {
    @EmbeddedId
    private StepCaseId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("testStepId")
    private TestStep testStep;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("testCaseId")
    private TestCase testCase;

    @Column(nullable = false)
    @CreationTimestamp
    private Instant createdDt;
    @Column(nullable = false)
    @UpdateTimestamp
    private Instant modifiedDt;

    private Integer orderNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        StepCase that = (StepCase) o;
        return Objects.equals(testStep, that.testStep) &&
                Objects.equals(testCase, that.testCase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testStep, testCase);
    }
}
