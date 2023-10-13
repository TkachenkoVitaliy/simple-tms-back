package ru.vtkachenko.simpletmsback.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.vtkachenko.simpletmsback.dto.TestSuiteDto;
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

    // TODO возможно стоит убрать метод
    @GetMapping
    public List<TestSuiteShortDto> getProjectShortTestSuites(@RequestParam Long projectId) {
        log.info("Request [/api/v1/suites] method [GET] - getProjectShortTestSuites. Request param - [projectId={}]",
                projectId);
        return testSuiteService.getShortTestSuites(projectId);
    }

    @GetMapping("/{id}")
    public TestSuiteDto getTestSuiteById(@PathVariable Long id) {
        log.info("Request [/api/v1/suites/{}] method [GET] - getTestSuiteById.", id);
        return testSuiteService.getTestSuiteById(id);
    }

    @PostMapping
    public TestSuiteDto createTestSuite(@Valid @RequestBody TestSuiteDto testSuiteDto) {
        log.info("Request [/api/v1/suites] method [POST] - createTestSuite. Request body - [{}]", testSuiteDto);
        return testSuiteService.createTestSuite(testSuiteDto);
    }

    @PutMapping
    public TestSuiteDto updateTestSuite(@Valid @RequestBody TestSuiteDto testSuiteDto) {
        log.info("Request [/api/v1/suites] method [PUT] - updateTestSuite. Request body - [{}]", testSuiteDto);
        return testSuiteService.updateTestSuite(testSuiteDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTestSuite(@PathVariable Long id) {
        log.info("Request [/api/v1/suites/{}] method [DELETE] - deleteTestSuite", id );
        testSuiteService.deleteTestSuite(id);
    }

}
