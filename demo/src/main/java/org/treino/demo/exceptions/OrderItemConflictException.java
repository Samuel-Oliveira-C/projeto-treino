package org.treino.demo.exceptions;

public class OrderItemConflictException extends RuntimeException {
    public OrderItemConflictException(String message) {
        super(message);
    }
}
