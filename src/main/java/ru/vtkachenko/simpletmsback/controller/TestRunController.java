package ru.vtkachenko.simpletmsback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vtkachenko.simpletmsback.model.TestRun;
import ru.vtkachenko.simpletmsback.service.TestRunService;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/v1/projects/{projectId}/runs", produces = "application/json")
@RequiredArgsConstructor
public class TestRunController {
    private final TestRunService testRunService;
    @GetMapping
    public TestRun testRun() {
        return testRunService.test();
    }
}
