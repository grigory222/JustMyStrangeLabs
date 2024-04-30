package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.requests.RegisterRequest;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.responses.RegisterResponse;
import ru.ifmo.se.dto.responses.Response;

public class RegisterWorker extends Worker{
    public RegisterWorker(Receiver receiver) {
        super(receiver);
    }

    public Response process(Request request){
        RegisterRequest req = (RegisterRequest) request;
        RegisterResponse rep = new RegisterResponse();

        int res = receiver.register(req.getLogin(), req.getPassword());
        rep.setSuccess(true);
        if (res == 0){
            rep.setSuccess(false);
        }
        if (res == -1) {
            rep.setSuccess(false);
            rep.setResultMessage("Пользователь с таким именем уже существует");
        }

        System.out.println("[DEBUG] Запрос на регистрацию пользователя " + req.getLogin() + " " + req.getPassword());

        return rep;
    }

}
