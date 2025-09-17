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
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InvestorService {

    private final InvestorMapper investorMapper;
    private final InvestorRepository investorRepository;
    private final AccountService accountService;
    private final InvestorValidator investorValidator;

    public InvestorService(InvestorMapper investorMapper,
                           InvestorRepository investorRepository,
                           AccountService accountService,
                           InvestorValidator investorValidator) {
        this.investorMapper = investorMapper;
        this.investorRepository = investorRepository;
        this.accountService = accountService;
        this.investorValidator = investorValidator;
    }

    @Transactional
    public InvestorResponseDTO create(CreateInvestorRequestDTO req) {
        investorValidator.validateForCreation(req);

        Investor investor = investorMapper.toEntity(req);

        String tempPassword = RandomPasswordGenerator.generatePassword(10);
        Account account = accountService.createAccount(
                req.account(),         // email vem aqui
                AccountRole.INVESTOR,
                tempPassword
        );

        investor.setAccount(account);
        account.setInvestor(investor);

        Investor saved = investorRepository.save(investor);

        // TODO: enviar e-mail com senha temporária quando implementar
        // emailService.sendInvestorWelcome(account.getEmail(), investor.getDisplayName(), tempPassword);
        System.out.println("Senha temporária do investidor: " + tempPassword);

        return investorMapper.toResponseDTO(saved);
    }

    @Transactional
    public InvestorResponseDTO updateInvestor(UUID id, UpdateInvestorRequestDTO dto) {
        Investor existing = getEntityById(id);
        investorValidator.validateForUpdate(existing, dto);

        investorMapper.updateInvestorFromDTO(dto, existing);
        Investor saved = investorRepository.save(existing);

        return investorMapper.toResponseDTO(saved);
    }


    @Transactional
    public PageResponseDTO<InvestorResponseDTO> getAll(String filter, Pageable pageable) {
        Page<Investor> page = investorRepository.search(filter, pageable);
        Page<InvestorResponseDTO> dtoPage = page.map(investorMapper::toResponseDTO);
        return PageResponseDTO.from(dtoPage);
    }

    @Transactional
    public InvestorResponseDTO getById(UUID id) {
        Investor investor = getEntityById(id);

        return investorMapper.toResponseDTO(investor);
    }

    public Investor getEntityById(UUID id) {
        return investorRepository.findById(id)
                .orElseThrow(() -> new NobileSolApiException(
                        "Investidor não encontrado com ID: " + id, HttpStatus.NOT_FOUND));
    }
}
