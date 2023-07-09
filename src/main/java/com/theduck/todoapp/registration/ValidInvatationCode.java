package com.theduck.todoapp.registration;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InvitationCodeValidator.class)
public @interface ValidInvatationCode {
    String message() default "Invalid invitation code.";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{};
}
