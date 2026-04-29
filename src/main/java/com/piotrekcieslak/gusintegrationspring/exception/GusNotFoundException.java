package com.piotrekcieslak.gusintegrationspring.exception;

public class GusNotFoundException extends RuntimeException {
    public GusNotFoundException(String message) {
        super(message);
    }
}