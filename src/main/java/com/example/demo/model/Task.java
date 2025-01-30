package com.example.demo.model;

import com.example.demo.dto.TaskDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.NOT_STARTED;

    @Column
    private LocalDateTime creationDate = LocalDateTime.now();

    public TaskDTO toTaskDTO() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setDescription(description);
        taskDTO.setStatus(status);
        taskDTO.setCreationDate(creationDate);
        return taskDTO;
    }
}
