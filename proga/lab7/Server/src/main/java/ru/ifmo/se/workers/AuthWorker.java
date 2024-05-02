package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.requests.AuthRequest;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.responses.AuthResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.network.JwtManager;

public class AuthWorker extends Worker{
    public AuthWorker(Receiver receiver, JwtManager jwtManager) {
        super(receiver, jwtManager);
    }

    public Response process(Request request){
        AuthRequest req = (AuthRequest) request;
        AuthResponse rep;
        System.out.println("[DEBUG] Запрос на авторизацию пользователя " + req.getLogin() + " " + req.getPassword());

        long id = receiver.auth(req.getLogin(), req.getPassword());
        if (id > 0) {
            rep = new AuthResponse(jwtManager.generateJwtToken(id));
            rep.setSuccess(true);
            System.out.println("[DEBUG] Выдан JWT токен пользователю " + req.getLogin() + ": " + rep.getToken());
        } else{
            rep = new AuthResponse(null);
            rep.setSuccess(false);
            System.out.println("[DEBUG] Неудачная попытка авторизации");
        }

        return rep;
    }

}
