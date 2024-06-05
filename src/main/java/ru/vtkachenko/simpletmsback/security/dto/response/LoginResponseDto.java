package ru.vtkachenko.simpletmsback.security.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoginResponseDto {
    private String token;
    private final String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String username;
    private List<String> roles;
}
