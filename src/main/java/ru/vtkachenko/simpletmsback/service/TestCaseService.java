package ru.vtkachenko.simpletmsback.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtkachenko.simpletmsback.exception.business.ProjectNotFoundException;
import ru.vtkachenko.simpletmsback.exception.business.TestCaseNotFoundException;
import ru.vtkachenko.simpletmsback.model.TestCase;
import ru.vtkachenko.simpletmsback.repository.TestCaseRepository;

@Slf4j
@Service
public class TestCaseService {
    private final TestCaseRepository testCaseRepository;

    @Autowired
    public TestCaseService(TestCaseRepository testCaseRepository) {
        this.testCaseRepository = testCaseRepository;
    }

    public TestCase getTestCase(Long id) {
        return testCaseRepository.findById(id).orElseThrow(() -> {
            String message = String.format("Cant find test case with id - %s", id);
            log.error(message);
            throw new TestCaseNotFoundException(message);
        });
    }
}
