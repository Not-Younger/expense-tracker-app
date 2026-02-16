package org.jonathanyoung.expensetrackerapp.controller;

import org.jonathanyoung.expensetrackerapp.model.Expense;
import org.jonathanyoung.expensetrackerapp.model.request.ExpenseRequest;
import org.jonathanyoung.expensetrackerapp.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Expense>> getAllExpenses() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        this.expenseService.findAllExpenses()
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenses(@PathVariable UUID id) {
        return this.expenseService.findExpense(id)
                .map(expense ->
                    ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(expense)
                )
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody ExpenseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(expenseService.addExpense(request));

    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable UUID id, @RequestBody ExpenseRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.expenseService.updateExpense(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable UUID id) {
        this.expenseService.deleteExpense(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
