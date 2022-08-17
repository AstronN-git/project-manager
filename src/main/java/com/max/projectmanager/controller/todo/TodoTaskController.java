package com.max.projectmanager.controller.todo;

import com.max.projectmanager.entity.Item;
import com.max.projectmanager.service.ItemService;
import com.max.projectmanager.service.ProjectService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("${url.todo}")
public class TodoTaskController {
    private final ProjectService projectService;
    private final ItemService itemService;

    @Value("${url.todo}")
    private String todoUrl;

    public TodoTaskController(ProjectService projectService, ItemService itemService) {
        this.projectService = projectService;
        this.itemService = itemService;
    }

    @GetMapping("${url.task.create}")
    public String showCreateTaskForm(Model model,
                                     @RequestParam(name = "projectid") Long projectId,
                                     @RequestParam(name = "isdone", required = false, defaultValue = "false") Boolean isDone) {
        var item = new Item();
        var project = projectService.findProjectById(projectId);
        if (project == null) {
            return "error";
        }

        project.addItem(item);
        item.setDone(isDone);
        model.addAttribute("item", item);
        return "create-task";
    }

    @PostMapping("${url.task.create}")
    public RedirectView processTaskCreation(@ModelAttribute Item item) {
        item = itemService.saveItem(item);
        return new RedirectView(todoUrl + "/?projectid=" + item.getProject().getId() + "&taskid=" + item.getId());
    }

    @GetMapping("${url.task.delete}")
    public RedirectView deleteTask(@RequestParam(name = "taskid") Long taskId) {
        var item = itemService.findItemById(taskId);
        if (item == null) {
            return new RedirectView(todoUrl);
        }

        var projectId = item.getProject().getId();

        itemService.deleteItem(item);

        return new RedirectView(todoUrl + "/?projectid=" + projectId);
    }

    @GetMapping("${url.task.done}")
    public RedirectView makeDone(@RequestParam(name = "taskid") Long taskId) {
        var item = itemService.findItemById(taskId);
        if (item == null) {
            return new RedirectView(todoUrl);
        }

        var projectId = item.getProject().getId();
        item.setDone(!item.getDone());
        itemService.saveItem(item);

        var itemId = item.getId();

        return new RedirectView(todoUrl + "/?projectid=" + projectId + "&taskid=" + itemId);
    }
}
