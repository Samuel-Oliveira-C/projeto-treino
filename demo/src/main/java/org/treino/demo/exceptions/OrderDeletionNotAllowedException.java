package org.treino.demo.exceptions;

public class OrderDeletionNotAllowedException extends RuntimeException {
    public OrderDeletionNotAllowedException(String message) {
        super(message);
    }
}
