package ru.vtkachenko.simpletmsback.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vtkachenko.simpletmsback.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public boolean isCredentialsAlreadyTaken(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            return true;
        }
        return userRepository.existsByEmail(email);
    }
}
