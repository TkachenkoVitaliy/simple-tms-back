package ru.vtkachenko.simpletmsback.service;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.vtkachenko.simpletmsback.dto.ProjectDto;
import ru.vtkachenko.simpletmsback.mapper.ProjectMapper;
import ru.vtkachenko.simpletmsback.model.Project;
import ru.vtkachenko.simpletmsback.repository.ProjectRepository;

import java.util.List;

@Slf4j
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

    public ProjectDto createProject(ProjectDto projectDto) {
//        try {
            Project project = ProjectMapper.toEntity(projectDto);
            project = saveProject(project);
            return ProjectMapper.toDto(project);
//        } catch (DataIntegrityViolationException e) {
//            if (e.getCause() instanceof ConstraintViolationException) {
//                log.error("ERROR {}", ((ConstraintViolationException) e.getCause()).getConstraintViolations());
//            }
//            throw new RuntimeException("SSS");
//        }
    }

    private Project saveProject(Project project) {
        return repository.save(project);
    }

    private List<Project> findAllProjects() {
        return repository.findAll();
    }
}
