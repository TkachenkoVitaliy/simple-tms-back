package ru.vtkachenko.simpletmsback.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.vtkachenko.simpletmsback.dto.response.TestSuiteShortDto;
import ru.vtkachenko.simpletmsback.service.TestSuiteService;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/v1/suites", produces = "application/json")
public class TestSuiteController {
    private final TestSuiteService testSuiteService;

    @Autowired
    public TestSuiteController(TestSuiteService testSuiteService) {
        this.testSuiteService = testSuiteService;
    }

    @GetMapping()
    public List<TestSuiteShortDto> getProjectShortTestSuites(@RequestParam Long projectId) {
        log.info("Request [/api/v1/suites] method [GET] - getProjectShortTestSuites. Request param - [projectId={}]",
                projectId);
        return testSuiteService.getShortTestSuites(projectId);
    }
}
