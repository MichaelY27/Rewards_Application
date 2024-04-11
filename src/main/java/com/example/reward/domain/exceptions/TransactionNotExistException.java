package com.example.reward.domain.exceptions;

public class TransactionNotExistException extends RuntimeException{
    public TransactionNotExistException(String message) {
        super(message);
    }
}
