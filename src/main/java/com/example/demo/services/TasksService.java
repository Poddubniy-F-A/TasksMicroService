package com.example.demo.services;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.TasksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        task.setStatus(TaskStatus.NOT_STARTED);
        task.setCreationDate(LocalDateTime.now());
        repository.save(task);
    }

    public List<Task> getByStatus(TaskStatus status) {
        return repository.findByStatus(status);
    }

    public void setStatus(Task task, TaskStatus status) {
        task.setStatus(status);
        repository.save(task);
    }

    public Task getById(Long id) {
        return repository.getById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
