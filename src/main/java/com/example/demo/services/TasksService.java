package com.example.demo.services;

import com.example.demo.dto.TaskCreateDTO;
import com.example.demo.dto.TaskDTO;
import com.example.demo.dto.TaskUpdateStatusDTO;
import com.example.demo.exceptions.TaskNotFoundException;
import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.TasksRepository;
import lombok.AllArgsConstructor;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TasksService {

    @MessagingGateway(defaultRequestChannel = "inputChannel")
    public interface LogGateway {
        void writeToFile(@Header(FileHeaders.FILENAME) String filename, String data);
    }

    private final TasksRepository repository;
    private final LogGateway logGateway;

    public List<TaskDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(Task::toTaskDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO addTask(TaskCreateDTO taskDTO) {
        Task task = taskDTO.toTask();
        logGateway.writeToFile(
                "log.txt",
                LocalDateTime.now() + ": CREATE\n" + task + "\n"
        );
        return repository.save(task).toTaskDTO();
    }

    public List<TaskDTO> getByStatus(TaskStatus status) {
        return repository.findByStatus(status)
                .stream()
                .map(Task::toTaskDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO setStatus(TaskUpdateStatusDTO taskDTO) throws TaskNotFoundException {
        Task task = repository.findById(taskDTO.getId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskDTO.getId()));
        task.setStatus(taskDTO.getStatus());
        logGateway.writeToFile(
                "log.txt",
                LocalDateTime.now() + ": STATUS UPDATE\n" + task + "\n"
        );
        return repository.save(task).toTaskDTO();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
