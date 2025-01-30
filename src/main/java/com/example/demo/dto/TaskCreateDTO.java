package com.example.demo.dto;

import com.example.demo.model.Task;
import lombok.Data;

@Data
public class TaskCreateDTO {
    private String description;

    public Task toTask() {
        Task task = new Task();
        task.setDescription(description);
        return task;
    }
}
