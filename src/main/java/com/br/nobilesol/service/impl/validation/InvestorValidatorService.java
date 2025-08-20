package com.br.nobilesol.service.impl.validation;

import com.br.nobilesol.dto.investor.CreateInvestorRequestDTO;
import com.br.nobilesol.entity.enums.InvestorType;
import com.br.nobilesol.exception.NobileSolApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class InvestorValidatorService {
    public void validateCreateRequest(CreateInvestorRequestDTO request) {
        if (request.investorType() == InvestorType.PF) {
            if (!StringUtils.hasText(request.name())) {
                throw new NobileSolApiException("Para investidor Pessoa Física, os campos 'name' (Nome) são obrigatórios.", HttpStatus.BAD_REQUEST);
            }
        } else if (request.investorType() == InvestorType.PJ) {
            if (!StringUtils.hasText(request.companyName())) {
                throw new NobileSolApiException("Para investidor Pessoa Jurídica, o campo 'companyName' (Razão Social) é obrigatório.", HttpStatus.BAD_REQUEST);
            }
        }
    }
}
