package com.br.nobilesol.service;

import com.br.nobilesol.entity.User;
import com.br.nobilesol.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public void changePassword(User user, String password, String confirmPassword) {
        user.setPasswordHash(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Usuário com o email: " + email + ", não encontrado")
        );
    }
}
