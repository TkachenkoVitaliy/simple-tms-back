package ru.vtkachenko.simpletmsback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtkachenko.simpletmsback.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
