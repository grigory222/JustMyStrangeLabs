package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.requests.AuthRequest;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.responses.AuthResponse;
import ru.ifmo.se.dto.responses.Response;

public class AuthWorker extends Worker{
    public AuthWorker(Receiver receiver) {
        super(receiver);
    }

    public Response process(Request request){
        AuthRequest req = (AuthRequest) request;
        AuthResponse rep = new AuthResponse();

        if (receiver.auth(req.getLogin(), req.getPassword()))
            rep.setSuccess(true);

        System.out.println("[DEBUG] Запрос на авторизацию пользователя " + req.getLogin() + " " + req.getPassword());

        return rep;
    }

}
