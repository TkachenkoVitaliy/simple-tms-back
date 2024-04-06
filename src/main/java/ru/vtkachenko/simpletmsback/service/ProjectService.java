package ru.vtkachenko.simpletmsback.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtkachenko.simpletmsback.dto.ProjectDto;
import ru.vtkachenko.simpletmsback.exception.business.ProjectNotFoundException;
import ru.vtkachenko.simpletmsback.mapper.ProjectMapper;
import ru.vtkachenko.simpletmsback.model.Project;
import ru.vtkachenko.simpletmsback.repository.ProjectRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper mapper;

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
        return projectRepository.findAllByOrderByCreatedDtDesc().stream()
                .map(mapper::toDto)
                .toList();
    }

    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> {
            String message = String.format("Cant find project with id - %s", id);
            log.error(message);
            return new ProjectNotFoundException(message);
        });
        return mapper.toDto(project);
    }

    @Transactional
    public ProjectDto createProject(ProjectDto projectDto) {
        Project project = mapper.toEntity(projectDto);
        project = projectRepository.save(project);
        return mapper.toDto(project);
    }

    @Transactional
    public ProjectDto updateProject(ProjectDto projectDto) {
        Project project = projectRepository.findById(projectDto.getId()).orElseThrow(() -> {
            String message = String.format("Cant update project with id - %s, cause project with this id not found", projectDto.getId());
            log.error(message);
            return new ProjectNotFoundException(message);
        });
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        return mapper.toDto(project);
    }

    @Transactional
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}
