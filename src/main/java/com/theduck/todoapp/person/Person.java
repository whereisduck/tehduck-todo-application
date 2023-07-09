package com.theduck.todoapp.person;

import com.theduck.todoapp.collaboration.TodoCollaborationRequest;
import com.theduck.todoapp.todo.Todo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity

public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @NotBlank
    @Column(unique = true)
    private String name;
    @Email
    @Column(unique = true)
    private String email;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "owner")//,fetch = FetchType.EAGER)
    private List<Todo> ownedTodos = new ArrayList<>();

    @ManyToMany(mappedBy = "collaborators")//, fetch = FetchType.EAGER)
    private List<Todo> collaborativeTodos = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "collaborator")//,fetch = FetchType.EAGER)
    private List<TodoCollaborationRequest> collaborationRequests = new ArrayList<>();

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Todo> getOwnedTodos() {
        return ownedTodos;
    }

    public void setOwnedTodos(List<Todo> ownedTodos) {
        this.ownedTodos = ownedTodos;
    }

    public List<Todo> getCollaborativeTodos() {
        return collaborativeTodos;
    }

    public void setCollaborativeTodos(List<Todo> collaborativeTodos) {
        this.collaborativeTodos = collaborativeTodos;
    }

    public List<TodoCollaborationRequest> getCollaborationRequests() {
        return collaborationRequests;
    }

    public void setCollaborationRequests(List<TodoCollaborationRequest> collaborationRequests) {
        this.collaborationRequests = collaborationRequests;
    }
}
