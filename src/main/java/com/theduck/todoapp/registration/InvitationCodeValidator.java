package com.theduck.todoapp.registration;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

public class InvitationCodeValidator implements ConstraintValidator<ValidInvatationCode, String> {
    private final Set<String> validInvitationCode;

    public InvitationCodeValidator(@Value("${custom.invitation-codes:none}")Set<String> validInvitationCode) {
        this.validInvitationCode = validInvitationCode;
    }

    @Override
    public void initialize(ValidInvatationCode constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()){
            return false;
        }
        return validInvitationCode.contains(value);
    }
}
