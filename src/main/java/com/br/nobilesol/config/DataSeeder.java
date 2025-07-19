
package com.br.nobilesol.config;

import com.br.nobilesol.entity.Employee;
import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.enums.AccountRole;
import com.br.nobilesol.repository.EmployeeRepository;
import com.br.nobilesol.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;

    public DataSeeder(AccountRepository accountRepository, PasswordEncoder passwordEncoder, EmployeeRepository employeeRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Executando DataSeeder para o perfil 'dev'...");
        createDefaultEmployee();
    }

    private void createDefaultEmployee() {
        if (accountRepository.count() == 0) {
            logger.info("Nenhum funcionário encontrado. Criando usuário 'admin' padrão...");

            Account newAccount = new Account();
            newAccount.setEmail("default@employee.com");
            newAccount.setName("Default Account");
            newAccount.setPasswordHash(passwordEncoder.encode("secret"));
            newAccount.setRole(AccountRole.EMPLOYEE);
            newAccount.setIsActive(true);

            Employee employeeProfile = new Employee();

            employeeProfile.setAccount(newAccount);
            newAccount.setEmployee(employeeProfile);
            employeeProfile.setAdmin(true);

            accountRepository.save(newAccount);
        } else {
            logger.info("Já existem funcionários no banco de dados. Nenhum usuário padrão foi criado.");
        }
    }
}
