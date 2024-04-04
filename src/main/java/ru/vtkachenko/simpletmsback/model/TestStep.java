package ru.vtkachenko.simpletmsback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "test_steps")
public class TestStep extends AbstractEntity {
    private String name;
    @NotNull
    private Boolean repeatable;
    @NotNull
    private String action;
    @ColumnDefault("''")
    private String expected;
    // TODO добавить поле с файлами (Set сущностей файла - id, link-ссылка на файл для скачивания/мб открытия онлайн, name)

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Project project;
    @JsonBackReference
    @OneToMany(mappedBy = "testStep", orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<TestCaseStep> testCases = new ArrayList<>();

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
