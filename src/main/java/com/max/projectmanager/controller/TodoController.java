package com.max.projectmanager.controller;

import com.max.projectmanager.entity.Item;
import com.max.projectmanager.entity.Project;
import com.max.projectmanager.service.ItemService;
import com.max.projectmanager.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/todo")
public class TodoController {
    private final ProjectService projectService;
    private final ItemService itemService;

    public TodoController(ProjectService projectService, ItemService itemService) {
        this.projectService = projectService;
        this.itemService = itemService;
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

    @GetMapping("/deleteproject")
    public RedirectView deleteProject(@RequestParam(name = "projectid") Long projectId) {
        var project = projectService.findProjectById(projectId);

        if (project != null) {
            projectService.deleteProject(project);
        }

        return new RedirectView("/todo/");
    }

    @GetMapping("/createproject")
    public String showCreateProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "create-project";
    }

    @PostMapping("/createproject")
    public RedirectView processProjectCreation(@ModelAttribute Project project) {
        project = projectService.saveProject(project);
        return new RedirectView("/todo/?projectid=" + project.getId());
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
        return new RedirectView("/todo/?projectid=" + item.getProject().getId() + "&taskid=" + item.getId());
    }

    @GetMapping("/deletetask")
    public RedirectView deleteTask(@RequestParam(name = "taskid") Long taskId) {
        var item = itemService.findItemById(taskId);
        if (item == null) {
            return new RedirectView("/todo/");
        }

        var projectId = item.getProject().getId();

        itemService.deleteItem(item);

        return new RedirectView("/todo/?projectid=" + projectId);
    }

    @GetMapping("/done")
    public RedirectView makeDone(@RequestParam(name = "taskid") Long taskId) {
        var item = itemService.findItemById(taskId);
        if (item == null) {
            return new RedirectView("/todo/");
        }

        var projectId = item.getProject().getId();
        item.setDone(true);
        itemService.saveItem(item);

        var itemId = item.getId();

        return new RedirectView("/todo/?projectid=" + projectId + "&taskid=" + itemId);
    }

    @GetMapping("/undone")
    public RedirectView makeUndone(@RequestParam(name = "taskid") Long taskId) {
        var item = itemService.findItemById(taskId);
        if (item == null) {
            return new RedirectView("/todo/");
        }

        var projectId = item.getProject().getId();
        item.setDone(false);
        itemService.saveItem(item);

        var itemId = item.getId();

        return new RedirectView("/todo/?projectid=" + projectId + "&taskid=" + itemId);
    }
}
