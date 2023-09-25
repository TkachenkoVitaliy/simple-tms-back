package ru.vtkachenko.simpletmsback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "test_suites")
public class TestSuite  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @CreationTimestamp
    private Instant createdDt;
    @Column(nullable = false)
    @UpdateTimestamp
    private Instant modifiedDt;
    // TODO: возможно стоит добавить @CreatedBy @LastModifiedBy
    // TODO: вынести в базовую хранимую сущность id, createDt, modifiedDt

    @NotNull
    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_suite_id")
    @ToString.Exclude
    private TestSuite parentSuite;
    @OneToMany(mappedBy = "parentSuite", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
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
