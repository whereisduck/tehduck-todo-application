package com.theduck.todoapp.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashBoardController {
    private final DashBoardService dashBoardService;
    @GetMapping
    public String getDashboard(Model model, @AuthenticationPrincipal OidcUser user){
        model.addAttribute("collaborators", List.of());

        if(user !=null){
            model.addAttribute("collaborators",dashBoardService.getAvailableCollaborator(user.getEmail()) );
            model.addAttribute("todos", dashBoardService.getAllOwnedAndSharedTodos(user.getEmail()));
        }
        return "dashboard";
    }
}
