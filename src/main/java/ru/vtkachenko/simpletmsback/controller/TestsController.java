package ru.vtkachenko.simpletmsback.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.vtkachenko.simpletmsback.dto.TestsTreeNodeDto;
import ru.vtkachenko.simpletmsback.dto.request.TestsTreeNodeUpdateParentDto;
import ru.vtkachenko.simpletmsback.service.TestsService;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/v1/tests", produces = "application/json")
public class TestsController {
    private final TestsService testsService;

    @Autowired
    public TestsController(TestsService testsService) {
        this.testsService = testsService;
    }

    @GetMapping("/{id}")
    public List<TestsTreeNodeDto> getProjectTestsTree(@PathVariable Long id) {
        log.info("Request [/api/v1/tests/{}] method [GET] - getProjectTestsTree.", id);
        return testsService.getTestsTreeByProject(id);
    }

    @PatchMapping("/{id}")
    public void updateTestsNodeParent(@RequestBody TestsTreeNodeUpdateParentDto updateParentDto, @PathVariable Long id) {
        log.info("Request [/api/v1/tests/{}] method [PATCH] - updateTestsNodeParent.", id);
        testsService.updateTestsTreeNodeParentSuite(id, updateParentDto);
    }
}
