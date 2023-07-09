package com.theduck.todoapp.dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollaboratorDto {
    private final Long id;
    private final String name;
}
