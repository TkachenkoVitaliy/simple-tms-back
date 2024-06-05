package ru.vtkachenko.simpletmsback.security.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRefreshResponseDto {
    private String accessToken;
    private String refreshToken;
    private final String tokenType = "Bearer";
}
