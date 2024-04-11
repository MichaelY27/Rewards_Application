package com.example.reward.domain.exceptions;

public class OperationUnableToProcessException extends RuntimeException{
    public OperationUnableToProcessException(String message) {
        super(message);
    }
}
