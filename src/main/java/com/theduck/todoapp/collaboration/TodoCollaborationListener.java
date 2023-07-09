package com.theduck.todoapp.collaboration;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TodoCollaborationListener {
    private final TodoCollaborationService todoCollaborationService;
    private final MailSender mailSender;
    @Value("${custom.confirm-email-from-address}")
    private String confirmationFromEmail;
    @Value("${custom.external-url}")
    private String externalUrl;

    @Value("${custom.auto-confirm-collaborations}")
    private boolean autoConfirmCollaborations;
    @SqsListener("${custom.sharing-queue}")
    public void confirmSharing(TodoCollaborationNotification payload) throws InterruptedException{
        log.info("incoming todo sharing payload: " + payload);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(confirmationFromEmail);
        message.setTo(confirmationFromEmail);
        message.setSubject("A todo was shared with you");
        message.setText(String.format(
                """
          Hi %s,\s

          someone shared a Todo from %s with you.

          Information about the shared Todo item:\s

          Title: %s\s
          Description: %s\s
          Priority: %s\s

          You can accept the collaboration by clicking this link: %s/todo/%s/collaborations/%s/confirm?token=%s\s

          Kind regards,\s
          Stratospheric""",
                payload.getCollaboratorEmail(),
                externalUrl,
                payload.getTodoTitle(),
                payload.getTodoDescription(),
                payload.getTodoPriority(),
                externalUrl,
                payload.getTodoId(),
                payload.getCollaboratorId(),
                payload.getToken()


        ));
        mailSender.send(message);
        log.info("Successfully informed collaborator about shared todo.");
        if (autoConfirmCollaborations) {
            log.info("Auto-confirmed collaboration request for todo: {}", payload.getTodoId());
            Thread.sleep(2_500);
            todoCollaborationService.confirmCollaboration(payload.getCollaboratorEmail(), payload.getTodoId(), payload.getCollaboratorId(), payload.getToken());
        }



    }


}
