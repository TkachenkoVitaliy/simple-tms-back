package ru.vtkachenko.simpletmsback.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.vtkachenko.simpletmsback.dto.TestCaseDto;
import ru.vtkachenko.simpletmsback.dto.TestStepDto;
import ru.vtkachenko.simpletmsback.service.TestCaseService;
import ru.vtkachenko.simpletmsback.service.TestStepService;

import java.util.List;

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
    public List<TestStepDto> getRepeatableTestSteps(@RequestParam Integer page, @RequestParam Integer pageSize) {
        log.info("Request [/apit/v1/steps/?page={}&pageSize={}] method [GET] - getRepeatableTestSteps", page, pageSize);
        return testStepService.getRepeatableSteps(page, pageSize);
    }
}
