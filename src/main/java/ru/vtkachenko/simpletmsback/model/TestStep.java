package ru.vtkachenko.simpletmsback.model;

import jakarta.persistence.*;
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
@Table(name = "test_steps")
public class TestStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @CreationTimestamp
    private Instant createdDt;
    @Column(nullable = false)
    @UpdateTimestamp
    private Instant modifiedDt;

    private String name;
    private Boolean repeatable;
    private String action;
    private String expected;
    // TODO добавить поле с файлами (Set сущностей файла - id, link-ссылка на файл для скачивания/мб открытия онлайн, name)
    // TODO private Integer orderNumber; - нужна промежуточная сущность для указания индекса степа в тест кейсе

    @ManyToOne(fetch = FetchType.LAZY)
    private TestSuite parentSuite;
    @OneToMany(mappedBy = "testStep", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StepCase> testCases = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TestStep testStep = (TestStep) o;
        return getId() != null && Objects.equals(getId(), testStep.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
