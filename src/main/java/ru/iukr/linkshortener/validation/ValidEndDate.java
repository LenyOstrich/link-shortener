package ru.iukr.linkshortener.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidFutureDateValidator.class)
public @interface ValidEndDate {
    String message() default "Некорректная дата";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
