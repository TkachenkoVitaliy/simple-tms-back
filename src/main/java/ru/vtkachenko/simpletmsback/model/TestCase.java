package ru.vtkachenko.simpletmsback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @ColumnDefault("")
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
