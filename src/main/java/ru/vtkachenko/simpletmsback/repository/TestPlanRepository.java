package ru.vtkachenko.simpletmsback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtkachenko.simpletmsback.model.TestPlan;

import java.util.List;

public interface TestPlanRepository extends JpaRepository<TestPlan, Long> {
    List<TestPlan> findTestPlansByProject_Id(Long projectId);

    Page<TestPlan> findTestPlansByProject_Id(Long projectId, Pageable pageable);
}
