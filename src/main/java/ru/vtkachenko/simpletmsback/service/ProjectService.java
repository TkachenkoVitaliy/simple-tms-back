package ru.vtkachenko.simpletmsback.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtkachenko.simpletmsback.dto.ProjectDto;
import ru.vtkachenko.simpletmsback.exception.business.ProjectNotFoundException;
import ru.vtkachenko.simpletmsback.mapper.ProjectMapper;
import ru.vtkachenko.simpletmsback.model.Project;
import ru.vtkachenko.simpletmsback.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;

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

    public ProjectDto getProjectById (Long id) {
        Project project = findProjectById(id).orElseThrow(() -> {
            String message = String.format("Cant update project with id - %s, cause project with this id not found", id);
            log.error(message);
            throw new ProjectNotFoundException(message);
        });
        return ProjectMapper.toDto(project);
    }

    @Transactional
    public ProjectDto createProject(ProjectDto projectDto) {
        Project project = ProjectMapper.toEntity(projectDto);
        project = saveProject(project);
        return ProjectMapper.toDto(project);
    }

    @Transactional
    public ProjectDto updateProject(ProjectDto projectDto) {
        repository.findById(projectDto.getId()).orElseThrow(() -> {
            String message = String.format("Cant update project with id - %s, cause project with this id not found", projectDto.getId());
            log.error(message);
            throw new ProjectNotFoundException(message);
        });
        Project project = saveProject(ProjectMapper.toEntity(projectDto));
        return ProjectMapper.toDto(project);
    }

    @Transactional
    public void deleteProject(Long id) {
        repository.deleteById(id);
    }

    private List<Project> findAllProjects() {
        return repository.findAllByOrderByCreatedDtDesc();
    }

    private Optional<Project> findProjectById(Long id) {
        return repository.findById(id);
    }

    private Project saveProject(Project project) {
        return repository.save(project);
    }
}
