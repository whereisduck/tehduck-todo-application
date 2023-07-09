package com.theduck.todoapp.collaboration;

import com.theduck.todoapp.person.Person;
import com.theduck.todoapp.todo.Todo;
import org.springframework.data.repository.CrudRepository;

public interface TodoCollaborationRequestRepository extends CrudRepository<TodoCollaborationRequest, Long> {
  TodoCollaborationRequest findByTodoAndCollaborator(Todo todo, Person person);
  TodoCollaborationRequest findByTodoIdAndCollaboratorId(Long todoId, Long collaboratorId);
}
