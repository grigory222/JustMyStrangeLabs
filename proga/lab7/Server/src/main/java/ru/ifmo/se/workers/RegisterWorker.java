package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.requests.RegisterRequest;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.responses.PrintUniqueDifficultyResponse;
import ru.ifmo.se.dto.responses.RegisterResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.network.JwtManager;

public class RegisterWorker extends Worker{
    public RegisterWorker(Receiver receiver, JwtManager jwtManager) {
        super(receiver, jwtManager);
    }

    public Response process(Request request){
        RegisterRequest req = (RegisterRequest) request;
        RegisterResponse rep = new RegisterResponse();

        int id = receiver.register(req.getLogin(), req.getPassword());
        if (id < 0){
            rep.setSuccess(false);
            rep.setResultMessage("Пользователь с таким именем уже существует");
        } else{
            rep.setSuccess(true);
            rep.setToken(jwtManager.generateJwtToken(id));
        }

        System.out.println("[DEBUG] Запрос на регистрацию пользователя " + req.getLogin() + " " + req.getPassword());
        System.out.println("[DEBUG] Выдан JWT токен пользователю " + req.getLogin() + ": " + rep.getToken());
        return rep;
    }

}
