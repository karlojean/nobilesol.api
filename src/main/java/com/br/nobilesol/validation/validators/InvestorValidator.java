package com.br.nobilesol.validation.validators;

import com.br.nobilesol.dto.investor.CreateInvestorRequestDTO;
import com.br.nobilesol.dto.investor.UpdateInvestorRequestDTO;
import com.br.nobilesol.entity.Investor;
import com.br.nobilesol.entity.enums.InvestorType;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.repository.InvestorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class InvestorValidator {

    private final InvestorRepository investorRepository;

    public void validateForCreation(CreateInvestorRequestDTO request) {
        if (investorRepository.existsByDocumentNumber(request.documentNumber())) {
            throw new NobileSolApiException("Documento já cadastrado.", HttpStatus.CONFLICT);
        }

        validateInvestorTypeConsistency(
                request.type(),
                request.name(),
                request.companyName(),
                request.tradeName()
        );
    }


    public void validateForUpdate(Investor existingInvestor, UpdateInvestorRequestDTO request) {
        String finalCompanyName = request.companyName() != null ? request.companyName() : existingInvestor.getCompanyName();
        String finalTradeName = request.tradeName() != null ? request.tradeName() : existingInvestor.getTradeName();
        String finalName = request.name() != null ? request.name() : existingInvestor.getName();

        validateInvestorTypeConsistency(
                existingInvestor.getType(),
                finalName,
                finalCompanyName,
                finalTradeName
        );
    }


    private void validateInvestorTypeConsistency(InvestorType type, String name, String companyName, String tradeName) {
        if (type == InvestorType.COMPANY) {
            if (!StringUtils.hasText(companyName)) {
                throw new NobileSolApiException("Razão Social é obrigatória para Pessoa Jurídica.", HttpStatus.BAD_REQUEST);
            }
        } else if (type == InvestorType.INDIVIDUAL) {
            if (!StringUtils.hasText(name)) {
                throw new NobileSolApiException("Nome é obrigatório para Pessoa Física.", HttpStatus.BAD_REQUEST);
            }
            if (StringUtils.hasText(companyName) || StringUtils.hasText(tradeName)) {
                throw new NobileSolApiException("Pessoa Física não pode ter Razão Social ou Nome Fantasia.", HttpStatus.BAD_REQUEST);
            }
        }
    }
}