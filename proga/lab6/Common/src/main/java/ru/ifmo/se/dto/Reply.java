package ru.ifmo.se.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Reply implements Serializable {
    protected boolean success;
}
