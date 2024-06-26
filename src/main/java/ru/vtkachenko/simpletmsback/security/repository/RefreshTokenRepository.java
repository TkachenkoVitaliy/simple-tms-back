package ru.vtkachenko.simpletmsback.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import ru.vtkachenko.simpletmsback.security.model.RefreshToken;
import ru.vtkachenko.simpletmsback.security.model.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    public int deleteByUser(User user);

    public Optional<RefreshToken> findByUser(User user);
}
