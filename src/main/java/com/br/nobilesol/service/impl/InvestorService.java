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
import com.br.nobilesol.service.impl.validation.InvestorValidatorService;
import com.br.nobilesol.utils.RandomPasswordGenerator;
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
    private final InvestorValidatorService investorValidatorService;

    public InvestorService(InvestorMapper investorMapper, InvestorRepository investorRepository, AccountService accountService, InvestorValidatorService investorValidatorService, InvestorValidatorService investorValidatorService1) {
        this.investorMapper = investorMapper;
        this.investorRepository = investorRepository;
        this.accountService = accountService;
        this.investorValidatorService = investorValidatorService1;
    }

    @Transactional
    public InvestorResponseDTO create(CreateInvestorRequestDTO createInvestorRequestDTO) {

        if (investorRepository.existsByDocumentNumber(createInvestorRequestDTO.documentNumber())) {
            throw new NobileSolApiException("Número de documento já está a ser utilizado por outro investidor.", HttpStatus.BAD_REQUEST);
        }

        investorValidatorService.validateCreateRequest(createInvestorRequestDTO);

        String password = RandomPasswordGenerator.generatePassword(10);

        Account account = accountService.createAccount(
                createInvestorRequestDTO.account(),
                AccountRole.INVESTOR,
                password
        );

        Investor investor = investorMapper.toEntity(createInvestorRequestDTO);
        investor.setAccount(account);
        account.setInvestor(investor);

        // Quando implementado, será enviado um email com a senha default
        System.out.println("Senha do usuário" + password);

        return investorMapper.toResponseDTO(investorRepository.save(investor));
    }

    @Transactional
    public InvestorResponseDTO updateInvestor(UUID id, UpdateInvestorRequestDTO request) {
        Investor investor = this.getEntityById(id);

        investorMapper.updateInvestorFromDto(request, investor);

        Investor savedInvestor = investorRepository.save(investor);
        return investorMapper.toResponseDTO(savedInvestor);
    }

    @Transactional
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
