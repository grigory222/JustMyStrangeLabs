package ru.ifmo.se.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenErrorResponse extends Response {
    private String errorMessage;

    public TokenErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
