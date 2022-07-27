package com.max.projectmanager.controller;

import com.max.projectmanager.entity.Item;
import com.max.projectmanager.entity.Project;
import com.max.projectmanager.service.ItemService;
import com.max.projectmanager.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MainController {
    private final ProjectService projectService;
    private final ItemService itemService;

    public MainController(ProjectService projectService, ItemService itemService) {
        this.projectService = projectService;
        this.itemService = itemService;
    }

    @GetMapping("/")
    public String index(Model model, @RequestParam(name = "projectid", required = false) Long projectId) {
        var projects = projectService.findAllProjects();
        model.addAttribute(projects);
        if (projectId != null) {
            var project = projectService.findProjectById(projectId);
            if (project != null) {
                var itemsDone = itemService.getItemsByProjectAndIsDone(project, true);
                var itemsNotDone = itemService.getItemsByProjectAndIsDone(project, false);
                model.addAttribute("itemsDone", itemsDone);
                model.addAttribute("itemsNotDone", itemsNotDone);
            }
            model.addAttribute("currentProjectId", projectId);
        }
        return "index";
    }

    @GetMapping("/deleteproject")
    public RedirectView deleteProject(@RequestParam(name = "projectid") Long projectId) {
        var project = projectService.findProjectById(projectId);
        if (project != null) {
            projectService.deleteProject(project);
        }
        return new RedirectView("/");
    }

    @GetMapping("/createproject")
    public String showCreateProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "create-project";
    }

    @PostMapping("/createproject")
    public RedirectView processProjectCreation(@ModelAttribute Project project) {
        project = projectService.saveProject(project);
        return new RedirectView("/?projectid=" + project.getId());
    }

    @GetMapping("/createtask")
    public String showCreateTaskForm(Model model,
                                     @RequestParam(name = "projectid") Long projectId,
                                     @RequestParam(name = "isdone", required = false, defaultValue = "false") Boolean isDone) {
        var item = new Item();
        var project = projectService.findProjectById(projectId);
        if (project == null) {
            return "error";
        }

        item.setProject(project);
        item.setDone(isDone);
        model.addAttribute("item", item);
        return "create-task";
    }

    @PostMapping("/createtask")
    public RedirectView processTaskCreation(@ModelAttribute Item item) {
        item = itemService.saveItem(item);
        return new RedirectView("/?projectid=" + item.getProject().getId());
    }
}
