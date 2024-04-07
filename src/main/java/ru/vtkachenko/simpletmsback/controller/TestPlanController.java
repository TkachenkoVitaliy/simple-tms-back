package ru.vtkachenko.simpletmsback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.vtkachenko.simpletmsback.dto.TestPlanDto;
import ru.vtkachenko.simpletmsback.service.TestPlanService;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/v1/projects/{projectId}/plans", produces = "application/json")
@RequiredArgsConstructor
public class TestPlanController {
    private final TestPlanService testPlanService;

    @GetMapping
    public List<TestPlanDto> getTestPlans(@PathVariable Long projectId) {
        log.info("Request [/api/v1/project/{}/plans] method [GET] - getTestPlans.", projectId);
        return testPlanService.getTestPlans(projectId);
    }

    @GetMapping("/{id}")
    public TestPlanDto getTestPlanById(@PathVariable Long projectId, @PathVariable Long id) {
        log.info("Request [/api/v1/projects/{}/plans/{}] method [GET] - getTestPlanById.",projectId, id);
        return testPlanService.getTestPlanById(projectId, id);
    }

    @PostMapping
    public TestPlanDto createTestPlan(@PathVariable Long projectId, @Valid @RequestBody TestPlanDto testPlanDto) {
        log.info("Request [/api/v1/projects/{}/plans] method [POST] - createTestPlan. Request body - [{}]",
                projectId, testPlanDto);
        return testPlanService.createTestPlan(projectId, testPlanDto);
    }
}
