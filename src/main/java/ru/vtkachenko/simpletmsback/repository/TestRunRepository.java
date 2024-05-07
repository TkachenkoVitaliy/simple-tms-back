package ru.vtkachenko.simpletmsback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.vtkachenko.simpletmsback.model.TestRun;

public interface TestRunRepository extends MongoRepository<TestRun, String> {
    Page<TestRun> findAllByProjectId(Long projectId, Pageable pageable);
}
