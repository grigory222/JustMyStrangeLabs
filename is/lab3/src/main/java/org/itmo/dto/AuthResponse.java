package org.itmo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private String username;
    private Set<String> roles;
    private String message;
    
    public AuthResponse(String username, Set<String> roles) {
        this.username = username;
        this.roles = roles;
    }
}
