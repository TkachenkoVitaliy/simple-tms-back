package ru.vtkachenko.simpletmsback.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.vtkachenko.simpletmsback.dto.TestStepDto;
import ru.vtkachenko.simpletmsback.dto.response.PageDto;
import ru.vtkachenko.simpletmsback.service.TestStepService;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/v1/steps", produces = "application/json")
public class TestStepController {
    private final TestStepService testStepService;

    @Autowired
    public TestStepController(TestStepService testStepService) {
        this.testStepService = testStepService;
    }

    @GetMapping
    public PageDto<TestStepDto> getRepeatableTestSteps(@RequestParam Integer page, @RequestParam Integer pageSize) {
        log.info("Request [/api/v1/steps/?page={}&pageSize={}] method [GET] - getRepeatableTestSteps", page, pageSize);
        return testStepService.getRepeatableSteps(page, pageSize);
    }
}
