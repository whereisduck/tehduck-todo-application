package com.theduck.todoapp.todo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/todo")
@RequiredArgsConstructor
@Slf4j
public class TodoController {
    private final TodoService todoService;
    @GetMapping("/show/{id}")
    public String showView(
            @AuthenticationPrincipal OidcUser user,
            @PathVariable("id") long id,
            Model model
            ){
        Todo todo = todoService.getOwnedOrSharedTodo(id, user.getEmail());
        model.addAttribute("todo", todo);
        MDC.put("todoId", String.valueOf(id));
        log.info("showing todo for userId {}", id);

        return "todo/show";
    }
    @GetMapping("/add")
    public String addView(Model model){
        model.addAttribute("todo", new Todo());
        model.addAttribute("editMode", EditMode.CREATE);
        return "todo/edit";
    }

    @PostMapping
    public String add(
            @Valid Todo toBeCreatedTodo,
            BindingResult bindingResult,
            @AuthenticationPrincipal OidcUser user,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("todo", toBeCreatedTodo);
            model.addAttribute("editMode", EditMode.CREATE);

            return "todo/edit";
        }

        Todo savedTodo = todoService.saveNewTodo(toBeCreatedTodo, user.getEmail(), user.getAttribute("name"));

        redirectAttributes.addFlashAttribute("message", "Your new todo has been successfully saved.");
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("todoId", savedTodo.getId());

        log.info("Successfully created todo");

        return "redirect:/dashboard";
    }
    @GetMapping("/edit/{id}")
    public String editView(
            @AuthenticationPrincipal OidcUser user,
            @PathVariable("id") long id,
            Model model
    ) {
        Todo todo = todoService.getOwnedOrSharedTodo(id, user.getEmail());

        model.addAttribute("todo", todo);
        model.addAttribute("editMode", EditMode.UPDATE);

        return "todo/edit";
    }
    @PostMapping("/update/{id}")
    public String update(
            @AuthenticationPrincipal OidcUser user,
            @PathVariable("id") long id,
            @Valid Todo updatedTodo,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            model.addAttribute("todo", updatedTodo);
            model.addAttribute("editMode", EditMode.UPDATE);

            return "todo/edit";
        }

        todoService.updateTodo(updatedTodo, id, user.getEmail());

        redirectAttributes.addFlashAttribute("message", "Your todo was successfully updated.");
        redirectAttributes.addFlashAttribute("messageType", "success");

        log.info("successfully updated todo");

        return "redirect:/dashboard";
    }
    @GetMapping("/delete/{id}")
    public String delete(
            @AuthenticationPrincipal OidcUser user,
            @PathVariable("id") long id,
            RedirectAttributes redirectAttributes
    ) {

        todoService.delete(id, user.getEmail());

        redirectAttributes.addFlashAttribute("message", "Your todo has been be deleted.");
        redirectAttributes.addFlashAttribute("messageType", "success");

        log.info("successfully deleted todo");

        return "redirect:/dashboard";
    }


}
