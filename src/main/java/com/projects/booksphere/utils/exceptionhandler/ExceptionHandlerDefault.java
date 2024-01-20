package com.projects.booksphere.utils.exceptionhandler;

import com.projects.booksphere.utils.exceptionhandler.exceptions.ElementAlreadyExistsException;
import com.projects.booksphere.utils.exceptionhandler.exceptions.ElementNotFoundException;
import com.projects.booksphere.utils.exceptionhandler.exceptions.ElementUpdateException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionHandlerDefault {

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<Problem> handleElementNotFoundException(ElementNotFoundException exception) {
        Problem problem = buildProblem(Status.NOT_FOUND, "Not found", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(ElementAlreadyExistsException.class)
    public ResponseEntity<Problem> handleElementAlreadyExistException(ElementAlreadyExistsException exception) {
        Problem problem = buildProblem(Status.CONFLICT, "Conflict", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(ElementUpdateException.class)
    public ResponseEntity<Problem> handleElementUpdateException(ElementUpdateException exception) {
        Problem problem = buildProblem(Status.BAD_REQUEST, "Bad request", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "Invalid request";
        Problem problem = buildProblem(Status.BAD_REQUEST, "Invalid request", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    private Problem buildProblem(Status status, String title, String detail) {
        return Problem.builder()
                .withStatus(status)
                .withTitle(title)
                .withDetail(detail)
                .build();
    }
}