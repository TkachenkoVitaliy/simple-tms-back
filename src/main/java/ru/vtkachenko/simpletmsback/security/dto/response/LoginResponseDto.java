package ru.vtkachenko.simpletmsback.security.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoginResponseDto {
    private Long id;
    private String token;
    private String refreshToken;
    private final String type = "Bearer";
    private String username;
    private List<String> roles;
}
