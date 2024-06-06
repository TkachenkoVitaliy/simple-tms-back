package ru.vtkachenko.simpletmsback.security.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class SignupRequestDto {
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @Builder.Default
    private Set<String> roles = new HashSet<>();
}
