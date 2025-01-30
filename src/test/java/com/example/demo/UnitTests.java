package com.example.demo;

import com.example.demo.dto.TaskCreateDTO;
import com.example.demo.dto.TaskDTO;
import com.example.demo.dto.TaskUpdateStatusDTO;
import com.example.demo.exceptions.TaskNotFoundException;
import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.TasksRepository;
import com.example.demo.services.TasksService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnitTests {
    @InjectMocks
    private TasksService service;
    @Mock
    private TasksRepository repository;
    @Mock
    private TasksService.LogGateway logGateway;

    @Test
    void getAll() {
        List<Task> tasks = List.of(new Task(), new Task());
        given(repository.findAll()).willReturn(tasks);

        List<TaskDTO> result = service.getAll();

        assertEquals(result, tasks.stream().map(Task::toTaskDTO).toList());
        verify(repository).findAll();
    }

    @Test
    void addTask() {
        String description = "description";
        TaskCreateDTO taskDTO = new TaskCreateDTO();
        taskDTO.setDescription(description);
        Task task = new Task();
        task.setDescription(description);
        given(repository.save(any(Task.class))).willReturn(task);

        TaskDTO result = service.addTask(taskDTO);

        assertEquals(result, task.toTaskDTO());
        verify(repository).save(any(Task.class));
        verify(logGateway).writeToFile(anyString(), anyString());
    }

    @Test
    void getByStatus() {
        TaskStatus status = TaskStatus.IN_PROCESS;
        Task task = new Task();
        task.setStatus(status);
        List<Task> tasks = List.of(task);
        given(repository.findByStatus(status)).willReturn(tasks);

        List<TaskDTO> result = service.getByStatus(status);

        assertEquals(result, tasks.stream().map(Task::toTaskDTO).toList());
        verify(repository).findByStatus(status);
    }

    @Test
    void setStatus() throws TaskNotFoundException {
        Long id = 1L;
        TaskStatus status = TaskStatus.IN_PROCESS;
        TaskUpdateStatusDTO updateDTO = new TaskUpdateStatusDTO();
        updateDTO.setId(id);
        updateDTO.setStatus(status);
        Task task = new Task(); //Тестовая задача
        task.setId(id);
        task.setStatus(status);
        given(repository.findById(id)).willReturn(Optional.of(task));
        given(repository.save(any(Task.class))).willReturn(task); // Исправлено

        TaskDTO result = service.setStatus(updateDTO);

        assertEquals(result, task.toTaskDTO());
        verify(repository).save(any(Task.class));
        verify(logGateway).writeToFile(anyString(), anyString());
    }

    @Test
    void setStatusIncorrect() {
        Long id = 1L;
        TaskUpdateStatusDTO updateDTO = new TaskUpdateStatusDTO();
        updateDTO.setId(id);
        updateDTO.setStatus(TaskStatus.IN_PROCESS);
        given(repository.findById(id)).willReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> service.setStatus(updateDTO));
        verify(repository).findById(id);
    }

    @Test
    void deleteById() {
        Long id = 1L;

        service.deleteById(id);

        verify(repository).deleteById(id);
    }
}
