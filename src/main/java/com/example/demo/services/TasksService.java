package com.example.demo.services;

import com.example.demo.exceptions.TaskNotFoundException;
import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.TasksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TasksService {
    private TasksRepository repository;

    public List<Task> getAll() {
        return repository.findAll();
    }

    public void addTask(String description) {
        Task task = new Task();
        task.setDescription(description);
        repository.save(task);
    }

    public List<Task> getByStatus(TaskStatus status) {
        return repository.findByStatus(status);
    }

    public Task setStatus(Long id, TaskStatus status) throws TaskNotFoundException {
        Optional<Task> response = repository.findById(id);
        if (response.isEmpty()) {
            throw new TaskNotFoundException();
        }

        Task task = response.get();
        task.setStatus(status);
        repository.save(task);

        return task;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
