package ru.ifmo.se.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterResponse extends Response{
    private String resultMessage;
    private String token;
}
