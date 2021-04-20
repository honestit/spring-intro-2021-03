package com.projects.ams.validation.validators;

import com.projects.ams.validation.constraints.StrongPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    public void initialize(StrongPassword constraint) {
    }

    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) return false;
        if (password.isBlank()) return false;
        if (password.trim().length() < 3) return false;
        if (password.trim().length() > 12) return false;
        if (!password.matches(".*[a-z].*")) return false;
        if (!password.matches(".*[A-Z].*")) return false;
        if (!password.matches(".*[0-9].*")) return false;
        if (!password.matches(".*[!@#$%].*")) return false;
        return true;
    }
}
