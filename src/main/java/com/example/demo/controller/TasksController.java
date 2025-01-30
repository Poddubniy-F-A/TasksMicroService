package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.exceptions.TaskNotFoundException;
import com.example.demo.model.TaskStatus;
import com.example.demo.services.TasksService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/tasks")
public class TasksController {

    private TasksService service;

    @GetMapping
    public List<TaskDTO> allTasks() {
        return service.getAll();
    }

    @PostMapping
    public ResponseEntity<TaskDTO> addTask(@RequestBody TaskCreateDTO taskDTO) {
        return new ResponseEntity<>(service.addTask(taskDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{status}")
    public List<TaskDTO> tasksByStatus(@PathVariable TaskStatus status) {
        return service.getByStatus(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTaskStatus(@PathVariable Long id, @RequestBody TaskUpdateStatusDTO taskDTO) {
        taskDTO.setId(id);
        try {
            TaskDTO updatedTask = service.setStatus(taskDTO);
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } catch (TaskNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
