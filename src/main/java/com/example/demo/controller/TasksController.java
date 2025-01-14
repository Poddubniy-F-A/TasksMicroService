package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.services.TasksService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/tasks")
public class TasksController {
    private TasksService service;

    @GetMapping
    public List<Task> getAllTasks() {
        return service.getAll();
    }

    @PostMapping
    public void addTask(@RequestBody String description) {
        service.addTask(description);
    }

    @GetMapping("/status/{status}")
    public List<Task> getTasksByStatus(@PathVariable TaskStatus status) {
        return service.getByStatus(status);
    }

    @PutMapping("/{id}")
    public void updateTaskStatus(@PathVariable Long id, @RequestBody TaskStatus status) {
        service.setStatus(service.getById(id), status);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        service.deleteById(id);
    }
}
