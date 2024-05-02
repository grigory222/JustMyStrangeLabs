package ru.ifmo.se.dto.responses;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Response implements Serializable {
    protected boolean success;
    protected boolean tokenError = false;
}
