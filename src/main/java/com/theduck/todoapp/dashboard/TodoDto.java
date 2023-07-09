package com.theduck.todoapp.dashboard;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class TodoDto {
    private Long id;
    private String title;
    private int amountOfCollaborators;
    private int amountOfCollaborationRequests;
    private LocalDate dueDate;
    private boolean isCollaboration;
}
