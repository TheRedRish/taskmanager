package com.example.taskmanager.controller;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskGroup;
import com.example.taskmanager.service.TaskGroupAssignmentService;
import com.example.taskmanager.service.TaskGroupService;
import com.example.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskGroupAssignmentService taskGroupAssignmentService;
    @Autowired
    private TaskGroupService taskGroupService;

    @PostMapping("/createTask/{groupId}")
    public String createTask(@RequestParam("name") String name,
                             @RequestParam("description") String description,
                             @RequestParam("priority") int priority,
                             @RequestParam("dueDate") LocalDate date,
                             @PathVariable("groupId") Long groupId) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setPriority(priority);
        task.setDueDate(date);
        if (groupId != 0){
            TaskGroup taskGroup = taskGroupService.getTaskGroupById(groupId);
            task.setTaskGroup(taskGroup);
            taskGroup.addTask(task);
        }
        taskService.saveTask(task);
        return "redirect:/";
    }

    @PostMapping("/updateTask")
    public String updateTask(@RequestParam("id") Long id,
                             @RequestParam("name") String name,
                             @RequestParam("description") String description) {
        Task task = taskService.getTaskById(id);
        task.setName(name);
        task.setDescription(description);
        taskService.saveTask(task);
        return "redirect:/";
    }

    @PostMapping("/updateTaskCompleted")
    public ResponseEntity<String> updateTask(@RequestParam("id") Long id,
                                             @RequestParam("completed") boolean completed) {
        Task task = taskService.getTaskById(id);
        task.setCompleted(completed);
        taskService.saveTask(task);
        return ResponseEntity.ok("Task updated successfully");
    }

    @PostMapping("/{taskId}/assign/{groupId}")
    public ResponseEntity<String> assignTaskToGroup(@PathVariable Long taskId, @PathVariable Long groupId) {
        taskGroupAssignmentService.assignTaskToGroup(taskId, groupId);
        return ResponseEntity.ok("Task assigned to group successfully");
    }

    @GetMapping("/{id}/edit")
    public String editTask(Model model, @PathVariable("id") Long id) {
        Task task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        return "editTask";
    }

    @GetMapping("/{id}/delete")
    public String deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }
}