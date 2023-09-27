package ru.vtkachenko.simpletmsback.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.vtkachenko.simpletmsback.dto.ProjectDto;
import ru.vtkachenko.simpletmsback.service.ProjectService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/projects", produces = "application/json")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<ProjectDto> getAllProjects() {
        log.info("Request [/api/v1/projects] method [GET] - getAllProjects.");
        return projectService.getAllProjects();
    }

    @PostMapping
    public ProjectDto createProject(@Valid @RequestBody ProjectDto projectDto) {
        log.info("Request [/api/v1/projects] method [POST] - createProject. Request body - [{}]", projectDto);
        return projectService.createProject(projectDto);
    }

    @PutMapping
    public ProjectDto updateProject(@Valid @RequestBody ProjectDto projectDto) {
        log.info("Request [/api/v1/projects] method [PUT] - updateProject. Request body - [{}]", projectDto);
        return projectService.updateProject(projectDto);
    }
}
