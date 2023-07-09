package com.theduck.todoapp.collaboration;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/todo")
public class TodoCollaborationController {
    private final TodoCollaborationService todoCollaborationService;

    @Timed(
            value = "collaboration.sharing",
            description = "Measure the time how long it takes to share a todo"
    )

    @PostMapping("/{todoId}/collaborations/{collaboratorId}")
    public String shareTodo(@PathVariable("todoId") Long todoId,
                            @PathVariable("collaboratorId") Long collaboratorId,
                            @AuthenticationPrincipal OidcUser principal,
                            RedirectAttributes redirectAttributes) {
        String collaboratorName = todoCollaborationService.shareWithCollaborator(principal.getEmail(), todoId, collaboratorId);
        redirectAttributes.addFlashAttribute("message", String.format("you successfully shared todo with %s." +
                "Once the user accepts the invite, you'll see them as a collaborator on your todo.", collaboratorName) );
        redirectAttributes.addFlashAttribute("messageType", "success");

        return "redirect:/dashboard";
    }

    @GetMapping("/{todoId}/collaborations/{collaboratorId}/confirm")
    public String confirmCollaboration(@PathVariable("todoId") Long todoId,
                                       @PathVariable("collaboratorId") Long collaboratorId,
                                       @AuthenticationPrincipal OidcUser principal,
                                       @RequestParam("token") String token,
                                       RedirectAttributes redirectAttributes){
        if (todoCollaborationService.confirmCollaboration(principal.getEmail(), todoId, collaboratorId, token)){
            redirectAttributes.addFlashAttribute("message","You've confirmed that you'd like to collaborate on this todo.");
            redirectAttributes.addFlashAttribute("messageType", "success");
        }else {
            redirectAttributes.addFlashAttribute("message", "not a valid request");
            redirectAttributes.addFlashAttribute("messageType","danger" );

        }
        return "redirect:/dashboard";


    }

    @DeleteMapping("/delete/{id}")
    public void deleteRequest(@PathVariable("id") Long id){
        todoCollaborationService.deletebyId(id);
    }



}
