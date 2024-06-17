package com.example.taskmanager.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "task_group")
public class TaskGroup implements Comparable<TaskGroup> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int priority;

    @OneToMany(mappedBy = "taskGroup")
    private List<Task> tasks;

    public TaskGroup(Long id, String name, int priority) {
        this.id = id;
        this.name = name;
        this.priority = priority;
    }

    public TaskGroup() {

    }

    public int getCompletedPercentage() {
        if (tasks == null || tasks.isEmpty()) {
            return 0;
        }
        long completedTasks = tasks.stream().filter(Task::isCompleted).count();
        return (int) ((double) completedTasks / tasks.size() * 100);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(TaskGroup o) {
        return this.priority - o.getPriority();
    }
}
