package ru.vtkachenko.simpletmsback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtkachenko.simpletmsback.model.StepCaseId;
import ru.vtkachenko.simpletmsback.model.StepCaseRel;

public interface StepCaseRelRepository extends JpaRepository<StepCaseRel, StepCaseId> {
}
