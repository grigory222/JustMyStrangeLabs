package org.itmo.controller;

import lombok.RequiredArgsConstructor;
import org.itmo.dto.AuthResponse;
import org.itmo.model.User;
import org.itmo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Контроллер для административных операций
 * Доступен только пользователям с ролью ADMIN
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    @GetMapping("/users")
    public ResponseEntity<String> getAllUsers(Authentication authentication) {
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        
        return ResponseEntity.ok("Admin endpoint accessed by: " + authentication.getName() + 
                " with roles: " + roles);
    }
    
    @PostMapping("/users/{username}/promote")
    public ResponseEntity<AuthResponse> promoteToAdmin(
            @PathVariable String username,
            Authentication authentication) {
        
        User user = userService.findByUsername(username);
        user.getRoles().add(User.Role.ROLE_ADMIN);
        
        // В реальном приложении здесь нужно сохранить изменения через repository
        
        Set<String> roles = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
        
        return ResponseEntity.ok(new AuthResponse(
                user.getUsername(), 
                roles, 
                "User promoted to admin successfully"
        ));
    }
}
