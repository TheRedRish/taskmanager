package com.example.taskmanager.service;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskGroup;
import com.example.taskmanager.repository.TaskGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TaskGroupService {
    @Autowired
    private TaskGroupRepository taskGroupRepository;
    @Autowired
    private TaskService taskService;

    public List<TaskGroup> getAllTaskGroups() {
        List<TaskGroup> taskGroups = taskGroupRepository.findAll();
        Collections.sort(taskGroups);
        return taskGroups;
    }

    public TaskGroup getTaskGroupById(Long id) {
        return taskGroupRepository.findById(id).orElse(null);
    }

    public TaskGroup saveTaskGroup(TaskGroup taskGroup) {
        return taskGroupRepository.save(taskGroup);
    }

    public void deleteTaskGroup(Long id, boolean deleteTasks) {
        TaskGroup taskGroup = getTaskGroupById(id);
        if (!taskGroup.getTasks().isEmpty()) {
            for (Task task : taskGroup.getTasks()) {
                if (deleteTasks) {
                    taskService.deleteTask(task.getId());
                } else {
                    task.setTaskGroup(null);
                }
            }
        }
        taskGroupRepository.deleteById(id);
    }
}
