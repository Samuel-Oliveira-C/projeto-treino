package org.treino.demo.controllers.handlersexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.treino.demo.exceptions.InvalidOrderStatusTransitionException;
import org.treino.demo.exceptions.InvalidOrderItemException;
import org.treino.demo.exceptions.InvalidPaymentException;
import org.treino.demo.exceptions.InsufficientStockException;
import org.treino.demo.exceptions.OrderDeletionNotAllowedException;
import org.treino.demo.exceptions.OrderItemConflictException;
import org.treino.demo.exceptions.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFound(
            ResourceNotFoundException exception) {
        return createProblem(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(InvalidOrderStatusTransitionException.class)
    public ResponseEntity<ProblemDetail> handleInvalidStatusTransition(
            InvalidOrderStatusTransitionException exception) {
        return createProblem(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(OrderDeletionNotAllowedException.class)
    public ResponseEntity<ProblemDetail> handleOrderDeletionNotAllowed(
            OrderDeletionNotAllowedException exception) {
        return createProblem(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(OrderItemConflictException.class)
    public ResponseEntity<ProblemDetail> handleOrderItemConflict(
            OrderItemConflictException exception) {
        return createProblem(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(InvalidOrderItemException.class)
    public ResponseEntity<ProblemDetail> handleInvalidOrderItem(
            InvalidOrderItemException exception) {
        return createProblem(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InvalidPaymentException.class)
    public ResponseEntity<ProblemDetail> handleInvalidPayment(
            InvalidPaymentException exception) {
        return createProblem(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ProblemDetail> handleInsufficientStock(
            InsufficientStockException exception) {
        return createProblem(HttpStatus.CONFLICT, exception.getMessage());
    }

    private ResponseEntity<ProblemDetail> createProblem(
            HttpStatus status,
            String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        return ResponseEntity.status(status).body(problem);
    }
}