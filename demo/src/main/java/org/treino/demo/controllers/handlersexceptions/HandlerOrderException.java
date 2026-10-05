package org.treino.demo.controllers.handlersexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.treino.demo.exceptions.InvalidOrderStatusTransitionException;
import org.treino.demo.exceptions.OrderDeletionNotAllowedException;
import org.treino.demo.exceptions.ResourceNotFoundException;

@RestControllerAdvice
public class HandlerOrderException {

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

    private ResponseEntity<ProblemDetail> createProblem(
            HttpStatus status,
            String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        return ResponseEntity.status(status).body(problem);
    }
}