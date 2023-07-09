package com.theduck.todoapp.todo;

import com.theduck.todoapp.person.Person;
import com.theduck.todoapp.collaboration.TodoCollaborationRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max=30)
    private String title;
    @Size(max=100)
    private String description;
    @NotNull
    private  Priority priority;
    @NotNull
    @DateTimeFormat(pattern = "yy-MM-dd")
    @Future
    private LocalDate dueDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,  CascadeType.REFRESH})
    @JoinColumn(name = "owner_id")//, referencedColumnName = "id")
    private Person owner;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)//,fetch = FetchType.EAGER)
    @JoinColumn(name = "todo_id")
    private List<Reminder> reminderList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)//, fetch = FetchType.EAGER)
    @JoinColumn(name = "todo_id")
    private List<Note> noteList = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "todo")//fetch = FetchType.EAGER )//mappedBy = "todo",fetch = FetchType.EAGER)
    //@JoinColumn(name = "todo_id")
    private List<TodoCollaborationRequest> toDoCollaborateRequestList = new ArrayList<>();


    @ManyToMany//(fetch = FetchType.EAGER)
    @JoinTable(name="todo_collaboration",
    joinColumns = @JoinColumn(name = "todo_id"),
    inverseJoinColumns = @JoinColumn(name = "collaborator_id"))
    private List<Person> collaborators = new ArrayList<>();

    public void addCollaborator(Person person) {
        this.collaborators.add(person);
        person.getCollaborativeTodos().add(this);
    }

    public void removeCollaborator(Person person) {
        this.collaborators.remove(person);
        person.getCollaborativeTodos().remove(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public List<Reminder> getReminderList() {
        return reminderList;
    }

    public void setReminderList(List<Reminder> reminderList) {
        this.reminderList = reminderList;
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    public List<TodoCollaborationRequest> getToDoCollaborateRequestList() {
        return toDoCollaborateRequestList;
    }

    public void setToDoCollaborateRequestList(List<TodoCollaborationRequest> toDoCollaborateRequestList) {
        this.toDoCollaborateRequestList = toDoCollaborateRequestList;
    }

    public List<Person> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<Person> collaborators) {
        this.collaborators = collaborators;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", dueDate=" + dueDate +
                ", status=" + status +
                ", owner=" + owner +
                '}';







}}
