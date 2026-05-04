package com.piotrekcieslak.gusintegrationspring.validation.regon;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegonValidator implements ConstraintValidator<ValidRegon, String> {

    @Override
    public boolean isValid(String regon, ConstraintValidatorContext context) {
        if (regon == null) {
            return false;
        }
        return regon.matches("\\d{9}|\\d{14}");
    }
}
