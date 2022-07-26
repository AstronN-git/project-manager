package com.max.projectmanager.controller;

import com.max.projectmanager.service.ItemService;
import com.max.projectmanager.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    private final ProjectService projectService;
    private final ItemService itemService;

    public MainController(ProjectService projectService, ItemService itemService) {
        this.projectService = projectService;
        this.itemService = itemService;
    }

    @GetMapping("/")
    public String index(Model model, @RequestParam(name = "projectId", required = false) Long projectId) {
        var projects = projectService.findAllProjects();
        model.addAttribute(projects);
        if (projectId != null) {
            var project = projectService.findProjectById(projectId);
            if (project != null) {
                var items = itemService.getItemsByProject(project);
                model.addAttribute("items", items);
            }
        }
        return "index";
    }
}
