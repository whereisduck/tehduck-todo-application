package com.theduck.todoapp.dashboard;

import com.theduck.todoapp.person.Person;
import com.theduck.todoapp.person.PersonRepository;
import com.theduck.todoapp.todo.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DashBoardService {
    private final PersonRepository personRepository;
    private final TodoRepository todoRepository;
    public List<CollaboratorDto> getAvailableCollaborator(String email){
        List<Person> collaborators = personRepository.findByEmailNot(email);
        return collaborators.stream()
                .map(person -> CollaboratorDto.builder().name(person.getName()).id(person.getId()).build())
                .collect(Collectors.toList());
    }
    public List<TodoDto> getAllOwnedAndSharedTodos(String email){
        List<TodoDto> ownedTodos = todoRepository.findAllByOwnerEmailOrderByIdAsc(email)
                .stream()
                .map(todo ->TodoDto.builder()
                        .title(todo.getTitle())
                        .id(todo.getId())
                        .dueDate(todo.getDueDate())
                        .isCollaboration(false)
                        .amountOfCollaborators(todo.getCollaborators().size())
                        .amountOfCollaborationRequests(todo.getToDoCollaborateRequestList().size())
                        .build() )
                .collect(Collectors.toList());
        List<TodoDto> collaborativeTodos = todoRepository.findAllByCollaboratorsEmailOrderByIdAsc(email)
                .stream()
                .map(todo ->TodoDto.builder()
                        .title(todo.getTitle())
                        .id(todo.getId())
                        .dueDate(todo.getDueDate())
                        .isCollaboration(true)
                        .amountOfCollaborators(todo.getCollaborators().size())
                        .amountOfCollaborationRequests(todo.getToDoCollaborateRequestList().size())
                        .build() )
                .collect(Collectors.toList());

        ownedTodos.addAll(collaborativeTodos);

        return ownedTodos;
    }



}
