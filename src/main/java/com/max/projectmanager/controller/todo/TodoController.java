package com.max.projectmanager.controller.todo;

import com.max.projectmanager.entity.Item;
import com.max.projectmanager.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.stream.Collectors;

@Controller
@RequestMapping("${url.todo}")
public class TodoController {
    private final ProjectService projectService;

    public TodoController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "projectid", required = false) Long projectId,
                        @RequestParam(name = "taskid", required = false) Long taskId) {
        var projects = projectService.findAllProjects();
        model.addAttribute(projects);

        if (projectId != null) {
            var project = projectService.findProjectById(projectId);

            if (project != null) {
                var allItems = project.getItems();

                var itemsDone = allItems.stream()
                        .filter(Item::getDone)
                        .collect(Collectors.toList());

                var itemsNotDone = allItems.stream()
                        .filter(item -> !item.getDone())
                        .collect(Collectors.toList());

                model.addAttribute("itemsDone", itemsDone);
                model.addAttribute("itemsNotDone", itemsNotDone);
            }

            model.addAttribute("currentProjectId", projectId);
        }

        if (taskId != null) {
            model.addAttribute("currentTaskId", taskId);
        }

        return "todo-index";
    }
}
