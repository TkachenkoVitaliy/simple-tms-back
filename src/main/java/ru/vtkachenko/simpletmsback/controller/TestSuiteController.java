package ru.vtkachenko.simpletmsback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.vtkachenko.simpletmsback.dto.TestSuiteDto;
import ru.vtkachenko.simpletmsback.dto.response.TestSuiteShortDto;
import ru.vtkachenko.simpletmsback.service.TestSuiteService;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/v1/projects/{projectId}/suites", produces = "application/json")
@RequiredArgsConstructor
public class TestSuiteController {
    private final TestSuiteService testSuiteService;

    // TODO возможно стоит убрать метод
    @GetMapping
    public List<TestSuiteShortDto> getProjectShortTestSuites(@PathVariable Long projectId) {
        log.info("Request [/api/v1/projects/{}/suites] method [GET] - getProjectShortTestSuites. Request param - [projectId={}]",
                projectId, projectId);
        return testSuiteService.getShortTestSuites(projectId);
    }

    @GetMapping("/{id}")
    public TestSuiteDto getTestSuiteById(@PathVariable Long projectId, @PathVariable Long id) {
        log.info("Request [/api/v1/projects/{}/suites/{}] method [GET] - getTestSuiteById.", projectId, id);
        return testSuiteService.getTestSuiteById(projectId, id);
    }

    @PostMapping
    public TestSuiteDto createTestSuite(@PathVariable Long projectId, @Valid @RequestBody TestSuiteDto testSuiteDto) {
        log.info("Request [/api/v1/projects/{}/suites] method [POST] - createTestSuite. Request body - [{}]",
                projectId, testSuiteDto);
        return testSuiteService.createTestSuite(testSuiteDto);
    }

    @PutMapping
    public TestSuiteDto updateTestSuite(@PathVariable Long projectId, @Valid @RequestBody TestSuiteDto testSuiteDto) {
        log.info("Request [/api/v1/projects/{}/suites] method [PUT] - updateTestSuite. Request body - [{}]",
                projectId, testSuiteDto);
        return testSuiteService.updateTestSuite(projectId, testSuiteDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTestSuite(@PathVariable Long projectId, @PathVariable Long id) {
        log.info("Request [/api/v1/projects/{}/suites/{}] method [DELETE] - deleteTestSuite", projectId, id );
        testSuiteService.deleteTestSuite(projectId, id);
    }

}
