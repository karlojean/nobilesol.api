package com.br.nobilesol.validation.validators;

import com.br.nobilesol.dto.project.CreateProjectRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProjectValidator {
    public void validateForCreation(CreateProjectRequestDTO request) {
        // TODO Add custom validation logic here if needed
    }
}
