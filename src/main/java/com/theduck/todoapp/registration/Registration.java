package com.theduck.todoapp.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Value
public class Registration {
    @Email
    private final String email;
    @NotBlank
    private final String username;
    @ValidInvatationCode
    private final String invitationCode;




    @Override
    public String toString() {
        return "Registration{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", invitationCode='" + invitationCode + '\'' +
                '}';
    }
}
