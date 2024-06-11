package ru.vtkachenko.simpletmsback.security.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.vtkachenko.simpletmsback.security.dto.request.TokenRefreshRequestDto;
import ru.vtkachenko.simpletmsback.security.dto.response.SignupResponseDto;
import ru.vtkachenko.simpletmsback.security.dto.response.TokenRefreshResponseDto;
import ru.vtkachenko.simpletmsback.security.model.RefreshToken;
import ru.vtkachenko.simpletmsback.security.repository.RoleRepository;
import ru.vtkachenko.simpletmsback.security.repository.UserRepository;
import ru.vtkachenko.simpletmsback.security.UserDetailsImpl;
import ru.vtkachenko.simpletmsback.security.dto.request.LoginRequestDto;
import ru.vtkachenko.simpletmsback.security.dto.request.SignupRequestDto;
import ru.vtkachenko.simpletmsback.security.dto.response.LoginResponseDto;
import ru.vtkachenko.simpletmsback.security.jwt.JwtUtils;
import ru.vtkachenko.simpletmsback.security.model.Role;
import ru.vtkachenko.simpletmsback.security.model.User;
import ru.vtkachenko.simpletmsback.security.service.AuthService;
import ru.vtkachenko.simpletmsback.security.service.RefreshTokenService;
import ru.vtkachenko.simpletmsback.security.service.RoleService;
import ru.vtkachenko.simpletmsback.security.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final RoleService roleService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    public LoginResponseDto authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        UserDetailsImpl userDetails = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

        String jwtToken = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        RefreshToken refreshToken = refreshTokenService.createOrUpdateRefreshToken(userDetails.getId());

        return LoginResponseDto.builder()
                .id(userDetails.getId())
                .token(jwtToken)
                .refreshToken(refreshToken.getToken())
                .username(userDetails.getUsername())
                .roles(roles)
                .build();
    }

    @PostMapping("/signup")
    public SignupResponseDto registerUser(@Valid @RequestBody SignupRequestDto signupRequest) {
        if (userService.isCredentialsAlreadyTaken(signupRequest.getUsername(), signupRequest.getEmail())) {
            return SignupResponseDto.builder()
                    .error("Login or email already taken")
                    .build();
        }
        Set<Role> roles = roleService.getRoles(signupRequest.getRoles());

        User user = User.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .roles(roles)
                .build();

        User savedUser = userRepository.save(user);

        LoginRequestDto loginRequestDto = new LoginRequestDto(savedUser.getUsername(), savedUser.getPassword());

        LoginResponseDto loginResponseDto = authenticateUser(loginRequestDto);

        return new SignupResponseDto(loginResponseDto);
    }

    @PostMapping("/refreshToken")
    public TokenRefreshResponseDto refreshToken(@Valid @RequestBody TokenRefreshRequestDto request) {
        RefreshToken refreshToken = refreshTokenService.verifyToken(request.getRefreshToken());
        String accessToken = jwtUtils.generateTokenFromUsername(refreshToken.getUser().getUsername());
        return TokenRefreshResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(request.getRefreshToken())
                .build();
    }

    @PostMapping("/signout")
    public String logoutUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        refreshTokenService.deleteByUserId(userId);
        return "Logout successful";
    }
}
