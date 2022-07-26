package com.max.projectmanager.service;

import com.max.projectmanager.entity.Project;
import com.max.projectmanager.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }
}
