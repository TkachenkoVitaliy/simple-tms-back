package ru.vtkachenko.simpletmsback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtkachenko.simpletmsback.model.TestCaseStepId;
import ru.vtkachenko.simpletmsback.model.TestCaseStep;

public interface StepCaseRelRepository extends JpaRepository<TestCaseStep, TestCaseStepId> {
}
