package com.br.nobilesol.service.impl;

import com.br.nobilesol.dto.PageResponseDTO;
import com.br.nobilesol.dto.investor.CreateInvestorRequestDTO;
import com.br.nobilesol.dto.investor.InvestorResponseDTO;
import com.br.nobilesol.dto.investor.UpdateInvestorRequestDTO;
import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.Investor;
import com.br.nobilesol.entity.enums.AccountRole;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.mapper.InvestorMapper;
import com.br.nobilesol.repository.InvestorRepository;
import com.br.nobilesol.utils.RandomPasswordGenerator;
import com.br.nobilesol.validation.validators.InvestorValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;


@Service
public class InvestorService {
    private final InvestorMapper investorMapper;
    private final InvestorRepository investorRepository;
    private final AccountService accountService;
    private final InvestorValidator investorValidator;

    public InvestorService(InvestorMapper investorMapper, InvestorRepository investorRepository, AccountService accountService, InvestorValidator investorValidator) {
        this.investorMapper = investorMapper;
        this.investorRepository = investorRepository;
        this.accountService = accountService;
        this.investorValidator = investorValidator;
    }

    @Transactional
    public InvestorResponseDTO create(CreateInvestorRequestDTO createInvestorRequestDTO) {
        investorValidator.validateForCreation(createInvestorRequestDTO);

        Investor investor = investorMapper.toEntity(createInvestorRequestDTO);

        String password = RandomPasswordGenerator.generatePassword(10);
        Account account = accountService.createAccount(
                createInvestorRequestDTO.account(),
                AccountRole.INVESTOR,
                password
        );
        investor.setAccount(account);
        account.setInvestor(investor);

        // Quando implementado, será enviado um email com a senha default
        System.out.println("Senha do usuário" + password);

        return investorMapper.toResponseDTO(investorRepository.save(investor));
    }

    @Transactional
    public InvestorResponseDTO updateInvestor(UUID id, UpdateInvestorRequestDTO updateDTO) {
        Investor existingInvestor = getEntityById(id);

        investorValidator.validateForUpdate(existingInvestor, updateDTO);

        investorMapper.updateInvestorFromDTO(updateDTO, existingInvestor);
        Investor savedInvestor = investorRepository.save(existingInvestor);


        return investorMapper.toResponseDTO(savedInvestor);
    }


    @Transactional()
    public PageResponseDTO<InvestorResponseDTO> getAll(String filter, Pageable pageable) {
        Page<Investor> investors = investorRepository.search(filter, pageable);
        Page<InvestorResponseDTO> dtoPage = investors.map(investorMapper::toResponseDTO);

        return PageResponseDTO.from(dtoPage);
    }

    public Investor getEntityById(UUID id) {
        return investorRepository.findById(id)
                .orElseThrow(() -> new NobileSolApiException("Investidor não encontrado com ID: " + id, HttpStatus.NOT_FOUND));
    }
}
