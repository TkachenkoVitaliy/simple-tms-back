package ru.vtkachenko.simpletmsback.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vtkachenko.simpletmsback.security.repository.RefreshTokenRepository;
import ru.vtkachenko.simpletmsback.security.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
}
