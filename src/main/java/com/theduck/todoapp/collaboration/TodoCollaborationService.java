package com.theduck.todoapp.collaboration;

import com.theduck.todoapp.person.Person;
import com.theduck.todoapp.person.PersonRepository;
import com.theduck.todoapp.todo.Todo;
import com.theduck.todoapp.todo.TodoRepository;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TodoCollaborationService {
    private final TodoCollaborationRequestRepository todoCollaborationRequestRepository;
    private final TodoRepository todoRepository;
    private final PersonRepository personRepository;
    private final SqsTemplate sqsTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Value("${custom.sharing-queue}")
    private String todoSharingQueueName;
    private static final String INVALID_TODO_ID = "Invalid todo ID: ";
    private static final String INVALID_PERSON_ID = "Invalid person ID: ";
    private static final String INVALID_PERSON_EMAIL = "Invalid person Email: ";
    public String shareWithCollaborator(String toDoOwnerEmail, Long todoId, Long collaboratorId){
        log.info("deleting request ");
        todoCollaborationRequestRepository.deleteById(35l);
        log.info("check if deleted");
        Todo todo = todoRepository.findAllByIdAndOwnerEmail(todoId, toDoOwnerEmail)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_TODO_ID + todoId));
        Person collaborator = personRepository.findById(collaboratorId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_PERSON_ID+collaboratorId));
        if (todoCollaborationRequestRepository.findByTodoAndCollaborator(todo, collaborator) !=null){
            log.info("Collaboration request for todo {} with collaborator {} already exists", todoId, collaboratorId);
            return collaborator.getName();
        }
        log.info("About to share todo with id {} with collaborator {}", todoId, collaboratorId);

        TodoCollaborationRequest collaboration = new TodoCollaborationRequest();
        String token = UUID.randomUUID().toString();
        collaboration.setToken(token);
        collaboration.setCollaborator(collaborator);
        collaboration.setTodo(todo);
        todo.getToDoCollaborateRequestList().add(collaboration);

        todoCollaborationRequestRepository.save(collaboration);

        sqsTemplate.send(todoSharingQueueName, new TodoCollaborationNotification(collaboration));

        return collaborator.getName();
    }


    public boolean confirmCollaboration(String email, Long todoId, Long collaboratorId, String token) {
        Person collaborator = personRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException(INVALID_PERSON_EMAIL+email));
        if(!collaborator.getId().equals(collaboratorId)){
            return false;
        }
        TodoCollaborationRequest todoCollaborationRequest = todoCollaborationRequestRepository.findByTodoIdAndCollaboratorId(todoId, collaboratorId);
        //TodoCollaborationRequest collaborationRequest = todoCollaborationRequestRepository.findByTodoIdAndCollaboratorId(todoId, collaboratorId);
        if(todoCollaborationRequest!=null && todoCollaborationRequest.getToken().equals(token)){
            log.info("collaboration request: {}" + todoCollaborationRequest);

        }else{return false;}
        log.info("original collaboration request token: {}" + token);
        log.info("confirm request token: {}", todoCollaborationRequest.getToken());

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(()->new IllegalArgumentException(INVALID_TODO_ID + todoId ));
        //todo.getCollaborators().add(collaborator);
        todo.addCollaborator(collaborator);
        //todoRepository.save(td);
        //todoCollaborationRequestRepository.delete(todoCollaborationRequest);
        todoCollaborationRequestRepository.delete(todoCollaborationRequest);
        String subject = "Collaboration confirmed.";
        String message = "User "+
                collaborator.getName() +
                " has accept your collaboration request for todo# " +
                todoId +
                ".";
        String ownerEmail = todoCollaborationRequest.getTodo().getOwner().getEmail();
        log.info("sending nitifiction to user " + ownerEmail);
        //SNS
        simpMessagingTemplate.convertAndSend("topic/todoUpdates/" + ownerEmail,  subject + " " + message);

        log.info("successfully inform owner about accepted request.");
        return true;

    }

    public void deletebyId(Long id){
        todoCollaborationRequestRepository.deleteById(id);
    }
}
