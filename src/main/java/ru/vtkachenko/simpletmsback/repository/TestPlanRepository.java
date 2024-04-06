package ru.vtkachenko.simpletmsback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtkachenko.simpletmsback.model.TestPlan;

public interface TestPlanRepository extends JpaRepository<TestPlan, Long> {
}
