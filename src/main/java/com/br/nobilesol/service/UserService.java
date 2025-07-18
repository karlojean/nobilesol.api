package com.br.nobilesol.service;

import com.br.nobilesol.dto.user.UserCredentialsRequestDTO;
import com.br.nobilesol.entity.User;
import com.br.nobilesol.entity.enums.UserRole;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(UserCredentialsRequestDTO userCredentialsRequest, UserRole role, String password) {
        if (userRepository.existsByEmail(userCredentialsRequest.email())) {
            throw new NobileSolApiException("Email já está a ser utilizado por outra conta.", HttpStatus.BAD_REQUEST);
        }

        User newUser = new User();
        newUser.setEmail(userCredentialsRequest.email());
        newUser.setName(userCredentialsRequest.name());
        newUser.setPasswordHash(passwordEncoder.encode(password));
        newUser.setRole(role);
        newUser.setIsActive(true);

        return userRepository.save(newUser);
    }

    public void changePassword(User user, String password, String confirmPassword) {
        user.setPasswordHash(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Usuário com o email: " + email + ", não encontrado")
        );
    }

    public boolean userExistsWithEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
