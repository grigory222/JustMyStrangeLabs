package web.backend.lab4.auth;

public class AuthValidator {
    boolean validateName(String name){
        return name != null && !name.isEmpty() && name.length() >= 4 && name.length() <= 16;
    }
    boolean validatePassword(String password){
        return password != null && !password.isEmpty() && password.length() >= 4 && password.length() <= 16;
    }
}
