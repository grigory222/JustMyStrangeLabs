package ru.ifmo.se.dto.requests;

import lombok.Getter;

@Getter
public class RegisterRequest extends Request{
    private final String login;
    private final String password;

    public RegisterRequest(String name, String login, String password) {
        super(name);
        this.login = login;
        this.password = password;
    }
}
