package com.piotrekcieslak.gusintegrationspring.exception;

public class GusAuthException extends RuntimeException {
    public GusAuthException(String message) {
        super(message);
    }
}