package com.example.demo;

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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnitTests {
    @InjectMocks
    private TasksService service;

    @Mock
    private TasksRepository repository;

    @Test
    public void testGetAll() {
        List<Task> tasks = Arrays.asList(new Task(), new Task());
        given(repository.findAll()).willReturn(tasks);

        List<Task> result = service.getAll();

        assertEquals(tasks, result);
        verify(repository).findAll();
    }

    @Test
    public void testAddTask() {
        String description = "some text";
        Task task = new Task();
        task.setDescription(description);

        service.addTask(description);

        verify(repository).save(task);
    }

    @Test
    public void testGetByStatus() {
        TaskStatus status = TaskStatus.NOT_STARTED;
        List<Task> tasks = Arrays.asList(new Task(), new Task());
        given(repository.findByStatus(status)).willReturn(tasks);

        List<Task> result = service.getByStatus(status);

        assertEquals(tasks, result);
        verify(repository).findByStatus(status);
    }

    @Test
    public void testSetStatus() throws TaskNotFoundException {
        Long id = 1L;
        TaskStatus status = TaskStatus.IN_PROCESS;
        Task task = new Task();
        task.setId(id);

        given(repository.findById(id)).willReturn(Optional.of(task));

        Task updatedTask = service.setStatus(id, status);

        assertEquals(status, updatedTask.getStatus());
        verify(repository).save(task);
    }

    @Test
    public void testSetStatus_TaskNotFound() {
        Long id = 1L;
        TaskStatus status = TaskStatus.IN_PROCESS;

        given(repository.findById(id)).willReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> service.setStatus(id, status));
        verify(repository, never()).save(any());
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;

        service.deleteById(id);

        verify(repository).deleteById(id);
    }
}
