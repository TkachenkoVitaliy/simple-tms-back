package ru.vtkachenko.simpletmsback.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtkachenko.simpletmsback.model.TestStep;
import ru.vtkachenko.simpletmsback.repository.TestStepRepository;

import java.util.List;

@Slf4j
@Service
public class TestStepService {

    private final TestStepRepository testStepRepository;

    @Autowired
    public TestStepService(TestStepRepository testStepRepository) {
        this.testStepRepository = testStepRepository;
    }

}
