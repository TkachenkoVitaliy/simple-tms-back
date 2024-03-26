package ru.vtkachenko.simpletmsback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtkachenko.simpletmsback.model.TestStep;

import java.util.List;

public interface TestStepRepository extends JpaRepository<TestStep, Long> {
    List<TestStep> findByTestCasesTestCaseId(Long testCaseId);

    Page<TestStep> findAllByRepeatableIsTrue(Pageable pageable);
}
