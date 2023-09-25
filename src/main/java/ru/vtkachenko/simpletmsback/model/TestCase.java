package ru.vtkachenko.simpletmsback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "test_cases")
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @CreationTimestamp
    private Instant createdDt;
    @Column(nullable = false)
    @UpdateTimestamp
    private Instant modifiedDt;

    @NotNull
    private String name;
    private String preconditions;

    @ManyToOne(fetch = FetchType.LAZY)
    private TestSuite parentSuite;
    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StepCase> testSteps = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TestCase testCase = (TestCase) o;
        return getId() != null && Objects.equals(getId(), testCase.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
