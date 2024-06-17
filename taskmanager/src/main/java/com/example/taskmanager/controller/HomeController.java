package com.example.taskmanager.controller;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskGroup;
import com.example.taskmanager.service.TaskGroupService;
import com.example.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private TaskGroupService taskGroupService;
    @Autowired
    private TaskService taskService;

    @GetMapping("/")
    public String home(Model model) {
        List<Task> unGroupedTasks = taskService.getUnGroupedTasks();
        model.addAttribute("unGroupedTasks", unGroupedTasks);
        List<TaskGroup> taskGroupList = taskGroupService.getAllTaskGroups();
        model.addAttribute("taskGroupList", taskGroupList);
        return "index";
    }
}
