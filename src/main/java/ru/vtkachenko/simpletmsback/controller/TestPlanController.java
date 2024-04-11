package ru.vtkachenko.simpletmsback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.vtkachenko.simpletmsback.dto.TestPlanDto;
import ru.vtkachenko.simpletmsback.dto.response.PageDto;
import ru.vtkachenko.simpletmsback.service.TestPlanService;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/v1/projects/{projectId}/plans", produces = "application/json")
@RequiredArgsConstructor
public class TestPlanController {
    private final TestPlanService testPlanService;

    @GetMapping
    public PageDto<TestPlanDto> getTestPlansPageable(
            @PathVariable Long projectId,
            @RequestParam Integer page,
            @RequestParam Integer pageSize
    ) {
        log.info("Request [/api/v1/project/{}/plans?page={}&pageSize={}] method [GET] - getTestPlansPageable",
                projectId, page, pageSize);
        return testPlanService.getTestPlansPageable(projectId, page, pageSize);
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

    @PutMapping
    public TestPlanDto updateTestSuite(@PathVariable Long projectId, @Valid @RequestBody TestPlanDto testPlanDto) {
        log.info("Request [/api/v1/projects/{}/plans] method [PUT] - updateTestSuite. Request body - [{}]",
                projectId, testPlanDto);
        return testPlanService.updateTestSuite(projectId, testPlanDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTestPlan(@PathVariable Long projectId, @PathVariable Long id) {
        log.info("Request [/api/v1/projects/{}/plans/{}] method [DELETE] - deleteTestPlan", projectId, id);
        testPlanService.deleteTestPlan(projectId, id);
    }
}
