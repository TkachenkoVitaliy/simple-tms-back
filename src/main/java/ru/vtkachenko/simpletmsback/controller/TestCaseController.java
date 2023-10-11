package ru.vtkachenko.simpletmsback.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.vtkachenko.simpletmsback.model.TestCase;
import ru.vtkachenko.simpletmsback.service.TestCaseService;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/v1/cases", produces = "application/json")
public class TestCaseController {
    private final TestCaseService testCaseService;

    @Autowired
    public TestCaseController(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    @GetMapping("/{id}")
    public TestCase getTestCaseSteps(@PathVariable Long id) {
        return testCaseService.getTestCase(id);
    }
}
