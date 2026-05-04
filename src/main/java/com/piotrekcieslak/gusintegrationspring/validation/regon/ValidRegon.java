package com.piotrekcieslak.gusintegrationspring.validation.regon;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RegonValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRegon {
    String message() default "REGON musi mieć 9 lub 14 cyfr";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
