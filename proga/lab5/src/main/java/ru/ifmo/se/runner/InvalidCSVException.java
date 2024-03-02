package ru.ifmo.se.runner;

public class InvalidCSVException extends Exception{
    InvalidCSVException(String errorMessage){
        //printStackTrace();
        //System.err.println(errorMessage);
        super(errorMessage);
    }
}
