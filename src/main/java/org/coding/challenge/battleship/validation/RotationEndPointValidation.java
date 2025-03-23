package org.coding.challenge.battleship.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RotationEndPointValidator.class)
public @interface RotationEndPointValidation{
    String message() default "rotation_end_point must be greater than rotation_start_point";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}