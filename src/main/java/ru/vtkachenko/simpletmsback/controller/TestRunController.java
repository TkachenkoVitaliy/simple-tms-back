package ru.vtkachenko.simpletmsback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.vtkachenko.simpletmsback.dto.TestRunDto;
import ru.vtkachenko.simpletmsback.dto.request.CreateTestRunDto;
import ru.vtkachenko.simpletmsback.dto.response.PageDto;
import ru.vtkachenko.simpletmsback.mapper.TestRunMapper;
import ru.vtkachenko.simpletmsback.model.TestRun;
import ru.vtkachenko.simpletmsback.service.TestRunService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/v1/projects/{projectId}/runs", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class TestRunController {
    private final TestRunService testRunService;
    private final TestRunMapper testRunMapper;

    @PostMapping
    public TestRunDto createTestRun(@PathVariable Long projectId, @Valid @RequestBody CreateTestRunDto request) {
        log.info("Request [/api/v1/projects/{}/runs] method [POST] - createTestRun. Request body - [{}]",
                projectId, request);
        TestRun testRun = testRunService.createTestRun(projectId, request);
        return testRunMapper.toDto(testRun);
    }

    @PutMapping
    public TestRunDto updateTestRun(@PathVariable Long projectId, @Valid @RequestBody TestRunDto testRunDto) {
        log.info("Request [/api/v1/projects/{}/runs] method [PUT] - updateTestRun. Request body - [{}]",
                projectId, testRunDto);
        TestRun testRun = testRunService.updateTestRun(projectId, testRunDto);
        return testRunMapper.toDto(testRun);
    }

    @GetMapping
    public PageDto<TestRunDto> getTestRunsPageable(
            @PathVariable Long projectId,
            @RequestParam Integer page,
            @RequestParam Integer pageSize
    ) {
        log.info("Request [/api/v1/project/{}/runs?page={}&pageSize={}] method [GET] - getTestPlansPageable",
                projectId, page, pageSize);
        PageDto<TestRun> testRunsPageable = testRunService.getTestRunsPageable(projectId, page, pageSize);
        List<TestRunDto> testRunDtos = testRunsPageable.getData().stream()
                .map(testRunMapper::toDto)
                .collect(Collectors.toList());

        return new PageDto<>(testRunsPageable.getTotalCount(), testRunDtos);
    }

    @GetMapping("/{id}")
    public TestRunDto getTestRun(@PathVariable Long projectId, @PathVariable String id) {
        log.info("Request [/api/v1/project/{}/runs/{}] method [GET] - getTestPlan",
                projectId, id);
        TestRun testRun = testRunService.getTestRun(projectId, id);
        return testRunMapper.toDto(testRun);
    }
}
