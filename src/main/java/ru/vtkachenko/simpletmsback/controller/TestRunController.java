package ru.vtkachenko.simpletmsback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.vtkachenko.simpletmsback.dto.request.CreateTestRunDto;
import ru.vtkachenko.simpletmsback.model.TestRun;
import ru.vtkachenko.simpletmsback.service.TestRunService;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/v1/projects/{projectId}/runs", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class TestRunController {
    private final TestRunService testRunService;
    @GetMapping
    public TestRun testRun() {
        return testRunService.test();
    }

    @PostMapping
    public TestRun createTestRun(@PathVariable Long projectId, @Valid @RequestBody CreateTestRunDto request) {
        log.info("Request [/api/v1/projects/{}/runs] method [POST] - createTestRun. Request body - [{}]",
                projectId, request);
        return testRunService.createTestRun(projectId, request);
    }
}
