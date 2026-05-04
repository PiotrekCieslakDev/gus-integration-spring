package com.piotrekcieslak.gusintegrationspring.validation.nip;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NipValidator implements ConstraintValidator<ValidNip, String> {

    @Override
    public boolean isValid(String nip, ConstraintValidatorContext context) {
        if (nip == null) {
            return false;
        }
        return nip.matches("\\d{10}");
    }
}
