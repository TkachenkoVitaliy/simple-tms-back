package ru.vtkachenko.simpletmsback.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.vtkachenko.simpletmsback.dto.TestsTreeNodeDto;
import ru.vtkachenko.simpletmsback.dto.request.TestsTreeNodeUpdateParentDto;
import ru.vtkachenko.simpletmsback.dto.response.TestNodeDto;
import ru.vtkachenko.simpletmsback.service.TestsService;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/v1/projects/{projectId}/tests", produces = "application/json")
@RequiredArgsConstructor
public class TestsController {
    private final TestsService testsService;

    @GetMapping
    public List<TestsTreeNodeDto> getProjectTestsTree(@PathVariable Long projectId) {
        log.info("Request [/api/v1/projects/{}/tests] method [GET] - getProjectTestsTree.", projectId);
        return testsService.getTestsTreeByProject(projectId);
    }

    @GetMapping("/nodes")
    public List<TestNodeDto> getTestNodesByProjectId(@PathVariable Long projectId) {
        log.info("Request [/api/v1/projects/{}/tests/nodes] method [GET] - getTestNodesByProjectId", projectId);
        return testsService.getTestsNodesByProject(projectId);
    }

    @PatchMapping("/{id}")
    public void updateTestsNodeParent(@RequestBody TestsTreeNodeUpdateParentDto updateParentDto, @PathVariable Long projectId, @PathVariable Long id) {
        log.info("Request [/api/v1/projects/{}/tests/{}] method [PATCH] - updateTestsNodeParent.", projectId, id);
        testsService.updateTestsTreeNodeParentSuite(id, updateParentDto);
    }
}
