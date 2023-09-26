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
    private Long id;
    @Column(nullable = false)
    @CreationTimestamp
    private Instant createdDt;
    @Column(nullable = false)
    @UpdateTimestamp
    private Instant modifiedDt;
    // TODO: возможно стоит добавить @CreatedBy @LastModifiedBy
    // TODO: вынести в базовую хранимую сущность id, createDt, modifiedDt
}