package ru.vtkachenko.simpletmsback.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.vtkachenko.simpletmsback.dto.TestCaseDto;
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
    public TestCaseDto getTestCaseById(@PathVariable Long id) {
        log.info("Request [/api/v1/cases/{}] method [GET] - getTestCaseById.", id);
        return testCaseService.getTestCase(id);
    }

    @PostMapping
    public TestCaseDto createTestCase(@Valid @RequestBody TestCaseDto testCaseDto) {
        log.info("Request [/api/v1/cases] method [POST] - createTestCase. Request body - [{}]", testCaseDto);
        return testCaseService.createTestCase(testCaseDto);
    }

    @PutMapping
    public TestCaseDto updateTestCase(@Valid @RequestBody TestCaseDto testCaseDto) {
        log.info("Request [/api/v1/cases] method [PUT] - updateTestCase. Request body - [{}]", testCaseDto);
        return testCaseService.updateTestCase(testCaseDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTestCase(@PathVariable Long id) {
        log.info("Request [/api/v1/cases/{}] method [DELETE] - deleteTestCase.", id);
        testCaseService.deleteTestCase(id);
    }
}
