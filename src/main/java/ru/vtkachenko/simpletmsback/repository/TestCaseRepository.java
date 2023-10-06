package ru.vtkachenko.simpletmsback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtkachenko.simpletmsback.model.Project;
import ru.vtkachenko.simpletmsback.model.TestCase;

import java.util.List;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    List<TestCase> findTestCaseByProject_Id(Long projectId);
}
