
package com.br.nobilesol.config;

import com.br.nobilesol.entity.Employee;
import com.br.nobilesol.entity.User;
import com.br.nobilesol.entity.enums.UserRole;
import com.br.nobilesol.repository.EmployeeRepository;
import com.br.nobilesol.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder, EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Executando DataSeeder para o perfil 'dev'...");
        createDefaultEmployee();
    }

    private void createDefaultEmployee() {
        if (userRepository.count() == 0) {
            logger.info("Nenhum funcionário encontrado. Criando usuário 'admin' padrão...");

            User newUser = new User();
            newUser.setEmail("default@employee.com");
            newUser.setName("Default User");
            newUser.setPasswordHash(passwordEncoder.encode("secret"));
            newUser.setRole(UserRole.EMPLOYEE);
            newUser.setIsActive(true);

            Employee employeeProfile = new Employee();

            employeeProfile.setUser(newUser);
            newUser.setEmployee(employeeProfile);

            userRepository.save(newUser);
        } else {
            logger.info("Já existem funcionários no banco de dados. Nenhum usuário padrão foi criado.");
        }
    }
}
