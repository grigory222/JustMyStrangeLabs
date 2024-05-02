package ru.ifmo.se.dto.requests;

import lombok.Getter;

@Getter
public class AuthRequest extends Request{
    private final String login;
    private final String password;

    public AuthRequest(String name, String login, String password) {
        super(name, null);
        this.login = login;
        this.password = password;
    }
}
