package com.secui.mvc.annotation;

import com.secui.mvc.validation.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface EmailValidation {

    String message() default "Email must be in valid format Like - example@gmail.com ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
