package ru.volkov.guest.util.exception;

public class AuthException extends RuntimeException {

    public AuthException(String message){
        super(message);
    }
}
