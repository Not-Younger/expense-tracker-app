package org.jonathanyoung.expensetrackerapp.controller;

import org.jonathanyoung.expensetrackerapp.model.Expense;
import org.jonathanyoung.expensetrackerapp.model.request.ExpenseRequest;
import org.jonathanyoung.expensetrackerapp.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        this.expenseService.findAllExpenses(authentication)
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenses(@PathVariable UUID id, Authentication authentication) {
        return this.expenseService.findExpense(id, authentication)
                .map(expense ->
                    ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(expense)
                )
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody ExpenseRequest request, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(expenseService.addExpense(request, authentication));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable UUID id, @RequestBody ExpenseRequest request, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.expenseService.updateExpense(id, request, authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable UUID id, Authentication authentication) {
        this.expenseService.deleteExpense(id, authentication);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
