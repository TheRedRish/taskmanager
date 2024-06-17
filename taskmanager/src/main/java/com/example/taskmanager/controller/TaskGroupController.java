package com.example.taskmanager.controller;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskGroup;
import com.example.taskmanager.service.TaskGroupService;
import com.example.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/groups")
public class TaskGroupController {
    @Autowired
    private TaskGroupService taskGroupService;

    @PostMapping("/createGroup")
    public String createTaskGroup(@RequestParam("name") String name) {
        TaskGroup group = new TaskGroup();
        group.setName(name);
        group.setPriority(0);
        taskGroupService.saveTaskGroup(group);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteTaskGroup(@RequestParam("groupId") Long id,
                                  @RequestParam("deleteTasks") boolean deleteTasks) {
        taskGroupService.deleteTaskGroup(id, deleteTasks);
        return "redirect:/";
    }
}
