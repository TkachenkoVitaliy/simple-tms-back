package ru.vtkachenko.simpletmsback.security.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SignupResponseDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refreshToken;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String username;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> roles;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String type = this.error == null ? "Bearer" : null;

    public SignupResponseDto(LoginResponseDto loginDto) {
        this.id = loginDto.getId();
        this.token = loginDto.getToken();
        this.refreshToken = loginDto.getRefreshToken();
        this.username = loginDto.getUsername();
        this.roles = loginDto.getRoles();
        this.error = null;
    }
}
