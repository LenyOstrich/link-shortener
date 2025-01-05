package ru.iukr.linkshortener.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Component
public class ValidEndDateValidator implements ConstraintValidator<ValidEndDate, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return LocalDateTime.parse(s).isAfter(LocalDateTime.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
