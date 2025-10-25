package com.br.nobilesol.validation.validators;

import com.br.nobilesol.dto.project.CreateProjectRequestDTO;
import com.br.nobilesol.exception.NobileSolApiException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class ProjectValidator {
    public void validateForCreation(CreateProjectRequestDTO request) {
        validateOperationDateNotBeforeConstructionStartDate(request.commercialOperationDate(), request.constructionStartDate());
        
        if(request.expectedAnnualGenerationWh() != null) {
            validateGenerationWithinEfficiencyRadius(request.expectedAnnualGenerationWh(), request.peakPowerWp());
        }

    }

    public void validateOperationDateNotBeforeConstructionStartDate(LocalDate operationDate, LocalDate constructionStartDate) {
        if (operationDate.isBefore(constructionStartDate)) {
            throw new NobileSolApiException("Data de operação não pode ser anterior à data de construção.", HttpStatus.BAD_REQUEST);
        }
    }

    public void validateGenerationWithinEfficiencyRadius(Long expectedAnnualGenerationWh, Long peakPowerWp) {
        double baselineAnnualWh = peakPowerWp * 4.50 * 0.80 * 365;
        double maxAnnualWh = baselineAnnualWh + (baselineAnnualWh * 0.3);
        double minAnnualWh = baselineAnnualWh - (baselineAnnualWh * 0.3);

        if(expectedAnnualGenerationWh > maxAnnualWh) {
            throw new NobileSolApiException("Geração anual esperada excede o limite máximo permitido para a potência instalada.", HttpStatus.BAD_REQUEST);
        }

        if(expectedAnnualGenerationWh < minAnnualWh) {
            throw new NobileSolApiException("Geração anual esperada está abaixo do limite mínimo permitido para a potência instalada.", HttpStatus.BAD_REQUEST);
        }
    }



}
