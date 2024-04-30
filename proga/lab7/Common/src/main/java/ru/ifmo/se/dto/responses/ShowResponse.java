package ru.ifmo.se.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowResponse extends Response {
    private String result;

    public ShowResponse() {
    }
}
