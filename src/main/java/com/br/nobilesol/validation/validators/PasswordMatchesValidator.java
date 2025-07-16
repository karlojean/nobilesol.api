package com.br.nobilesol.validation.validators;

import com.br.nobilesol.dto.auth.ResetPasswordRequestDTO;
import com.br.nobilesol.validation.anotations.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, ResetPasswordRequestDTO> {

    @Override
    public boolean isValid(ResetPasswordRequestDTO dto, ConstraintValidatorContext context) {
        return Objects.equals(dto.password(), dto.confirmPassword());
    }
}