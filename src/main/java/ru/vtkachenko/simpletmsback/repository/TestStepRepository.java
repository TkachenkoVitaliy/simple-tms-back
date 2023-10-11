package ru.vtkachenko.simpletmsback.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtkachenko.simpletmsback.model.TestStep;

import java.util.List;

public interface TestStepRepository extends JpaRepository<TestStep, Long> {
    List<TestStep> findByTestCasesTestCaseId(Long testCaseId);
}
