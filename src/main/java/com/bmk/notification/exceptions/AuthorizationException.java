package com.bmk.notification.exceptions;

public class AuthorizationException extends Exception{
    public AuthorizationException() {
        super("Invalid token");
    }
}