package com.example.demo.dto;

import com.example.demo.model.TaskStatus;
import lombok.Data;

@Data
public class TaskUpdateStatusDTO {
    private Long id;
    private TaskStatus status;
}
