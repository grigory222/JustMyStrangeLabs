package ru.ifmo.se.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse extends Response{
    private final String token;

    public AuthResponse(String token) {
        this.token = token;
    }
}
