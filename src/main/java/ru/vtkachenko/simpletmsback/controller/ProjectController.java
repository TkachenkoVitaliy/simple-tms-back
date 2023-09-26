package ru.vtkachenko.simpletmsback.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public List<ProjectDto> getAllProject() {
        log.debug("Request [/api/v1/projects] method [GET]");
        return projectService.getAllProjects();
    }
}
