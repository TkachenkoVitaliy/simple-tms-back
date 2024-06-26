package ru.vtkachenko.simpletmsback.security.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.vtkachenko.simpletmsback.constant.enums.SystemRole;
import ru.vtkachenko.simpletmsback.model.AbstractEntity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@ToString
@Entity
@Table(name = "roles")
public class Role extends AbstractEntity {
    @Enumerated(EnumType.STRING)
    private SystemRole name;
}
