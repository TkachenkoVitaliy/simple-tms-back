package ru.vtkachenko.simpletmsback.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.vtkachenko.simpletmsback.model.TestRun;

public interface TestRunRepository extends MongoRepository<TestRun, String> {
}
