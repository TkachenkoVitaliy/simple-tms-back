package ru.vtkachenko.simpletmsback.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vtkachenko.simpletmsback.service.TestPlanService;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/v1/projects/{projectId}/plans", produces = "application/json")
@RequiredArgsConstructor
public class TestPlanController {
    private final TestPlanService testPlanService;
}
