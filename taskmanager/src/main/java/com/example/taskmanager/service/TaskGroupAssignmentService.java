package com.example.taskmanager.service;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskGroupAssignmentService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskGroupService taskGroupService;

    public void assignTaskToGroup(Long taskId, Long groupId) {
        Task task = taskService.getTaskById(taskId);
        TaskGroup group = taskGroupService.getTaskGroupById(groupId);

        if (task != null) {
            task.setTaskGroup(group);
            if (group != null) {
                group.addTask(task);
            }

            taskService.saveTask(task);
        }
    }

    // Other methods related to task group assignments, if needed
}