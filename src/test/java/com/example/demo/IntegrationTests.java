package com.example.demo;

import com.example.demo.exceptions.TaskNotFoundException;
import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.TasksRepository;
import com.example.demo.services.TasksService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class IntegrationTests {
    @Autowired
    private TasksService service;

    @MockitoBean
    private TasksRepository repository;

    @Test
    public void testGetAll() {
        List<Task> tasks = Arrays.asList(new Task(), new Task());
        when(repository.findAll()).thenReturn(tasks);

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
        when(repository.findByStatus(status)).thenReturn(tasks);

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

        when(repository.findById(id)).thenReturn(Optional.of(task));

        Task updatedTask = service.setStatus(id, status);

        assertEquals(status, updatedTask.getStatus());
        verify(repository).save(task);
    }

    @Test
    public void testSetStatus_TaskNotFound() {
        Long id = 1L;
        TaskStatus status = TaskStatus.IN_PROCESS;

        when(repository.findById(id)).thenReturn(Optional.empty());

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
