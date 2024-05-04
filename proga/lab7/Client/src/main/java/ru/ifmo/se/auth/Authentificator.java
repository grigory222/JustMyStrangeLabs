package ru.ifmo.se.auth;

import lombok.Getter;
import ru.ifmo.se.dto.requests.AuthRequest;
import ru.ifmo.se.dto.requests.RegisterRequest;
import ru.ifmo.se.dto.responses.AuthResponse;
import ru.ifmo.se.dto.responses.RegisterResponse;
import ru.ifmo.se.network.Network;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public final class Authentificator {
    private final Socket socket;
    private final Console console = System.console();
    private final BufferedReader reader;
    private final PrintWriter printWriter;
    @Getter
    private String login;
    @Getter
    private String password;

    public Authentificator(PrintWriter printWriter, BufferedReader reader, Socket socket) {
        this.socket = socket;
        this.reader = reader;
        this.printWriter = printWriter;
    }

    public String auth() {
        String token = null;
        while (token == null) {
            try {
                printWriter.println("Выберите действие:");
                printWriter.println("1. Вход");
                printWriter.println("2. Регистрация");
                printWriter.print("> ");
                printWriter.flush();
                String choice = reader.readLine();

                switch (choice.trim()) {
                    case "1":
                        token = login();
                        break;
                    case "2":
                        token = register();
                        break;
                    default:
                        printWriter.println("Неверный выбор. Попробуйте снова.");
                }

                if (token != null) {
                    printWriter.println("Вход выполнен успешно!");
                }

            } catch (IOException e) {
                printWriter.println("Ошибка чтения. Завершение программы...");
                break;
            }
        }
        return token;
    }

    private String login() {
        String token = null;
        try {
            printWriter.print("Введите логин: "); printWriter.flush();
            login = reader.readLine();

            printWriter.print("Введите пароль: "); printWriter.flush();
            //password = new String(System.console().readPassword());
            password = new Scanner(System.in).nextLine();
            token = remoteAuth(login, password);
            if (token == null){
                printWriter.println("Ошибка аутентификации. Проверьте логин и пароль.");
            }

        } catch (IOException e) {
            printWriter.println("Ошибка чтения...");
        }
        return token;
    }

    String result = "";
    private String register() {
        String token = null;
        try {
            printWriter.print("Введите желаемый логин: "); printWriter.flush();
            login = reader.readLine();

            printWriter.print("Введите пароль: "); printWriter.flush();
            password = new String(System.console().readPassword());
            //password = reader.readLine();

            printWriter.print("Повторите пароль: "); printWriter.flush();
            String passwordRepeat = new String(System.console().readPassword());
            //String passwordRepeat = reader.readLine();

            if (!passwordRepeat.equals(password)){
                printWriter.println("Пароли не совпадают! Попробуйте снова");
                return null;
            }
            token = remoteRegister(login, password);
            if (token != null) {
                printWriter.println("Регистрация выполнена успешно!");
            } else {
                printWriter.println("Ошибка регистрации");
                printWriter.println(result);
            }

        } catch (IOException e) {
            printWriter.println("Ошибка чтения...");
        }
        return token;
    }

    private String remoteRegister(String login, String password) {
        RegisterRequest req = new RegisterRequest("reg", login, password);
        RegisterResponse response = (RegisterResponse) Network.sendAndReceive(socket, req);
        if (response != null && response.isSuccess()){
            result = response.getResultMessage();
            return response.getToken();
        }
        return null;
    }

    private String remoteAuth(String login, String password) {
        AuthRequest req = new AuthRequest("auth", login, password);
        AuthResponse response = (AuthResponse) Network.sendAndReceive(socket, req);
        if (response != null && response.isSuccess()){
            return response.getToken();
        }
        return null;
    }

}
