package ru.vtkachenko.simpletmsback.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // TODO в классах Project, TestSuite, TestCase можем использовать IDENTITY т.к. сохраняем сущности только по одной за раз
    // TODO но в классе TestStep может быть создано несколько за раз - может быть стоит использовать SEQUENCE с allocationSize
    // https://jpa-buddy.com/blog/the-ultimate-guide-on-db-generated/
    private Long id;
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdDt;
    @Column(nullable = false)
    @UpdateTimestamp
    private Instant modifiedDt;
    // TODO: возможно стоит добавить @CreatedBy @LastModifiedBy
}