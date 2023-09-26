package ru.vtkachenko.simpletmsback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtkachenko.simpletmsback.dto.ProjectDto;
import ru.vtkachenko.simpletmsback.mapper.ProjectMapper;
import ru.vtkachenko.simpletmsback.model.Project;
import ru.vtkachenko.simpletmsback.repository.ProjectRepository;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository repository;

    @Autowired
    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public List<ProjectDto> getAllProjects() {
        return findAllProjects().stream()
                .map(ProjectMapper::toDto)
                .toList();
    }

    private List<Project> findAllProjects() {
        return repository.findAll();
    }
}
