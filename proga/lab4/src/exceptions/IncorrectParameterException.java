package exceptions;


// бросается когда программист передал неверный параметр
public class IncorrectParameterException extends RuntimeException{
    public IncorrectParameterException(String message){
        super(message);
    }
    public IncorrectParameterException(Throwable cause){
        super(cause);
    }
}