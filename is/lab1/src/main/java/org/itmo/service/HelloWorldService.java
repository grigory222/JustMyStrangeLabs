package org.itmo.service;

import org.springframework.stereotype.Service;

@Service
public class HelloWorldService {

    public String getGreetingMessage() {
        return "Привет из Service слоя!";
    }
}