package exceptions;

// бросается когда пользователь пытается смешать/сжечь/растворить вещество с нулевой массой или объёмом
public class ZeroSubstancePropertyException extends Exception{
    public ZeroSubstancePropertyException(String message){
        super(message);
    }
}