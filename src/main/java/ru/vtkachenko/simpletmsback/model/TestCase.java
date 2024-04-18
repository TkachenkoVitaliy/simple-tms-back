package ru.vtkachenko.simpletmsback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;
import ru.vtkachenko.simpletmsback.constant.enums.CasePriority;
import ru.vtkachenko.simpletmsback.constant.enums.CaseType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@NamedEntityGraph(
        name = "TestCase.testSteps.testStep",
        attributeNodes = @NamedAttributeNode(value = "testSteps", subgraph = "testSteps-subgraph"),
        subgraphs = @NamedSubgraph(
                name = "testSteps-subgraph",
                attributeNodes = @NamedAttributeNode("testStep")
        )
)
@Table(name = "test_cases")
public class TestCase extends AbstractEntity {
    @NotNull
    private String name;
    @NotNull
    @Enumerated(EnumType.STRING)
    private CaseType type;
    @NotNull
    @Enumerated(EnumType.STRING)
    private CasePriority priority;
    @ColumnDefault("''")
    private String preconditions;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Project project;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private TestSuite parentSuite;
    @JsonManagedReference
    @OneToMany(mappedBy = "testCase", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<TestCaseStep> testSteps = new ArrayList<>();
    @ManyToMany
    @JoinTable(name="plan_case")
    private Set<TestPlan> testPlans = new HashSet<>();

    public void removeAllTestSteps() {
        this.testSteps.forEach(stepCaseRel -> {
            stepCaseRel.setTestCase(null);
            stepCaseRel.setTestStep(null);
        });
        this.testSteps.clear();
    }

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
