package ru.vtkachenko.simpletmsback.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtkachenko.simpletmsback.model.Project;
import ru.vtkachenko.simpletmsback.model.TestCase;

import java.util.List;
import java.util.Optional;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

    List<TestCase> findTestCaseByProject_Id(Long projectId);
    Optional<TestCase> findById(@NotNull Long testCaseId);

//    @EntityGraph(attributePaths = {"testSteps", "testSteps.testStep"}, type = EntityGraph.EntityGraphType.LOAD)
//    Optional<TestCase> getTestCaseById(@NotNull Long testCaseId);

    @EntityGraph(value = "TestCase.testSteps.testStep", type = EntityGraph.EntityGraphType.LOAD)
    Optional<TestCase> getTestCaseById(@NotNull Long testCaseId);
}
