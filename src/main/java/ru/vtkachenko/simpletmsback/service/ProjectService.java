package ru.vtkachenko.simpletmsback.service;

import jakarta.persistence.EntityNotFoundException;
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
    private final ProjectRepository projectRepository;
    private final ProjectMapper mapper;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProjectMapper mapper) {
        this.projectRepository = projectRepository;
        this.mapper = mapper;
    }

    public Project getProjectReferenceById(Long projectId) {
        try {
            return projectRepository.getReferenceById(projectId);

        } catch (EntityNotFoundException e) {
            String message = String.format("Cant find project with id - %s", projectId);
            log.error(message);
            throw new ProjectNotFoundException(message);
        }
    }

    public List<ProjectDto> getAllProjects() {
        return findAllProjects().stream()
                .map(mapper::toDto)
                .toList();
    }

    public ProjectDto getProjectById (Long id) {
        Project project = findProjectById(id).orElseThrow(() -> {
            String message = String.format("Cant find project with id - %s", id);
            log.error(message);
            throw new ProjectNotFoundException(message);
        });
        return mapper.toDto(project);
    }

    @Transactional
    public ProjectDto createProject(ProjectDto projectDto) {
        Project project = mapper.toEntity(projectDto);
        project = saveProject(project);
        return mapper.toDto(project);
    }

    @Transactional
    public ProjectDto updateProject(ProjectDto projectDto) {
//        projectRepository.findById(projectDto.getId()).orElseThrow(() -> {
//            String message = String.format("Cant update project with id - %s, cause project with this id not found", projectDto.getId());
//            log.error(message);
//            throw new ProjectNotFoundException(message);
//        });
        Project project = projectRepository.findById(projectDto.getId()).orElseThrow(() -> new RuntimeException(""));
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        return mapper.toDto(project);
    }

    @Transactional
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    private List<Project> findAllProjects() {
        return projectRepository.findAllByOrderByCreatedDtDesc();
    }

    private Optional<Project> findProjectById(Long id) {
        return projectRepository.findById(id);
    }

    private Project saveProject(Project project) {
        return projectRepository.save(project);
    }
}
