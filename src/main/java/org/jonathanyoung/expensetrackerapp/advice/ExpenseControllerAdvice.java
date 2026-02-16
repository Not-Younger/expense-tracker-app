package org.jonathanyoung.expensetrackerapp.advice;

import org.jonathanyoung.expensetrackerapp.Exception.ExpenseException;
import org.jonathanyoung.expensetrackerapp.model.response.ExpenseErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExpenseControllerAdvice {

    @ExceptionHandler(ExpenseException.class)
    public ResponseEntity<ExpenseErrorResponse> handleExpenseException(ExpenseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExpenseErrorResponse(e.getMessage()));
    }
}
