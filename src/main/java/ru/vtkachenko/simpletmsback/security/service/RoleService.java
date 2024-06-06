package ru.vtkachenko.simpletmsback.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vtkachenko.simpletmsback.constant.enums.SystemRole;
import ru.vtkachenko.simpletmsback.repository.RoleRepository;
import ru.vtkachenko.simpletmsback.security.model.Role;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Set<Role> getRoles(Collection<String> strRoles) {
        Set<Role> roles = new HashSet<>();
        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(SystemRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
            return roles;
        }
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
        return roles;
    }

}
