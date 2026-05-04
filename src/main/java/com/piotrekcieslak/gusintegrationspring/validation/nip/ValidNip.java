package com.piotrekcieslak.gusintegrationspring.validation.nip;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NipValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNip {
    String message() default "NIP musi składać się z dokładnie 10 cyfr";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
