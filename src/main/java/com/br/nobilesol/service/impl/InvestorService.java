package com.br.nobilesol.service.impl;

import com.br.nobilesol.dto.investor.CreateInvestorRequestDTO;
import com.br.nobilesol.dto.investor.InvestorResponseDTO;
import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.Investor;
import com.br.nobilesol.entity.enums.AccountRole;
import com.br.nobilesol.mapper.InvestorMapper;
import com.br.nobilesol.repository.InvestorRepository;
import com.br.nobilesol.utils.RandomPasswordGenerator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
public class InvestorService {
    private final InvestorMapper investorMapper;
    private final InvestorRepository investorRepository;
    private final AccountService accountService;

    public InvestorService(InvestorMapper investorMapper, InvestorRepository investorRepository, AccountService accountService) {
        this.investorMapper = investorMapper;
        this.investorRepository = investorRepository;
        this.accountService = accountService;
    }

    @Transactional
    public InvestorResponseDTO create(CreateInvestorRequestDTO createInvestorRequestDTO) {
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
}
