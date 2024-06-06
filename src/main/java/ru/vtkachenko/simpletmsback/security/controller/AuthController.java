package ru.vtkachenko.simpletmsback.security.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.vtkachenko.simpletmsback.constant.enums.SystemRole;
import ru.vtkachenko.simpletmsback.repository.RoleRepository;
import ru.vtkachenko.simpletmsback.repository.UserRepository;
import ru.vtkachenko.simpletmsback.security.UserDetailsImpl;
import ru.vtkachenko.simpletmsback.security.dto.request.LoginRequestDto;
import ru.vtkachenko.simpletmsback.security.dto.request.SignupRequestDto;
import ru.vtkachenko.simpletmsback.security.dto.response.LoginResponseDto;
import ru.vtkachenko.simpletmsback.security.jwt.JwtUtils;
import ru.vtkachenko.simpletmsback.security.model.Role;
import ru.vtkachenko.simpletmsback.security.model.User;
import ru.vtkachenko.simpletmsback.security.service.AuthService;

import java.util.HashSet;
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

        return LoginResponseDto.builder()
                .id(userDetails.getId())
                .token(jwtToken)
                .username(userDetails.getUsername())
                .roles(roles)
                .build();
    }

    @PostMapping("/signup")
    public String registerUser(@Valid @RequestBody SignupRequestDto signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return "ERROR";
        }
        if(userRepository.existsByEmail(signupRequest.getEmail())) {
            return "ERROR";
        }

        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(SystemRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(SystemRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(SystemRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        User user = User.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .roles(roles)
                .build();

        User savedUser = userRepository.save(user);

        return savedUser.getId().toString();
    }
}
