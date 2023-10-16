package ru.vtkachenko.simpletmsback.model;

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
@ToString
@Entity
@Table(name = "test_suites")
public class TestSuite extends AbstractEntity {

    @NotNull
    private String name;
    @ColumnDefault("")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Project project;
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_suite_id")
    @ToString.Exclude
    private TestSuite parentSuite;
    @OneToMany(mappedBy = "parentSuite", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<TestCase> testCases = new ArrayList<>();

    public void addTestCase(TestCase testCase) {
        testCases.add(testCase);
        testCase.setParentSuite(this);
    }

    public void removeTestCase(TestCase testCase) {
        testCases.remove(testCase);
        testCase.setParentSuite(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TestSuite testSuite = (TestSuite) o;
        return getId() != null && Objects.equals(getId(), testSuite.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
