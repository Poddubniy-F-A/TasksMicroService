package com.example.demo.dto;

import com.example.demo.model.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDTO {
    private String description;
    private TaskStatus status;
    private LocalDateTime creationDate;
}
