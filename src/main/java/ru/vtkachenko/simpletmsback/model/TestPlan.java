package ru.vtkachenko.simpletmsback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "test_plans")
public class TestPlan extends AbstractEntity {
    @NotNull
    private String name;
    @ColumnDefault("''")
    private String description;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Project project;
    // TODO нужен список testCase, но нужен ли их порядок? Если да, то нужна дополнительная таблица и будут сложности
    // с маппингом
}
