package com.max.projectmanager.controller.todo;

import com.max.projectmanager.entity.Project;
import com.max.projectmanager.service.ProjectService;
import com.max.projectmanager.util.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("${url.todo}")
public class TodoProjectController {
    private final ProjectService projectService;

    @Value("${url.todo}")
    String todoUrl;

    public TodoProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("${url.project.delete}")
    public String deleteProject(@RequestParam(name = "projectid") Long projectId) {
        var project = projectService.findProjectById(projectId);

        if (project != null) {
            projectService.deleteProject(project);
        }

        return "redirect:" + todoUrl;
    }

    @GetMapping("${url.project.create}")
    public String showCreateProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "create-project";
    }

    @PostMapping("${url.project.create}")
    public String processProjectCreation(@ModelAttribute Project project) {
        project.setUser(Users.getCurrentUser());
        project = projectService.saveProject(project);
        return "redirect:" + todoUrl + "/?projectid=" + project.getId();
    }
}
