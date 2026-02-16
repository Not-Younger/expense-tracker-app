package org.jonathanyoung.expensetrackerapp.service;

import org.jonathanyoung.expensetrackerapp.Exception.ExpenseException;
import org.jonathanyoung.expensetrackerapp.model.Category;
import org.jonathanyoung.expensetrackerapp.model.DTO.ExpenseDTO;
import org.jonathanyoung.expensetrackerapp.model.Expense;
import org.jonathanyoung.expensetrackerapp.model.request.ExpenseRequest;
import org.jonathanyoung.expensetrackerapp.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> findAllExpenses() {
        return this.expenseRepository.findAll().stream()
                .map(expenseDTO -> Expense.builder()
                        .id(expenseDTO.getId())
                        .category(Category.valueOf(expenseDTO.getCategory()))
                        .price(expenseDTO.getPrice())
                        .date(expenseDTO.getDate())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public Optional<Expense> findExpense(UUID id) {
        return this.expenseRepository.findById(id)
                .map(expenseDTO -> Expense.builder()
                        .id(expenseDTO.getId())
                        .category(Category.valueOf(expenseDTO.getCategory()))
                        .price(expenseDTO.getPrice())
                        .date(expenseDTO.getDate())
                        .build()
                );
    }

    public Expense addExpense(ExpenseRequest request) {
        Category category = checkStringCategory(request.category());

        ExpenseDTO expenseDTO = ExpenseDTO.builder()
                .category(category.toString())
                .price(request.price())
                .date(Instant.now())
                .build();

        return Stream.of(this.expenseRepository.save(expenseDTO))
                .map(newExpenseDTO -> Expense.builder()
                        .id(expenseDTO.getId())
                        .category(Category.valueOf(expenseDTO.getCategory()))
                        .price(expenseDTO.getPrice())
                        .date(expenseDTO.getDate())
                        .build()
                )
                .findFirst()
                .orElseThrow(() -> new ExpenseException("There was an exception when saving the expense."));
    }

    public Expense updateExpense(UUID id, ExpenseRequest request) {
        ExpenseDTO existingExpenseDTO = this.expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseException("Could not find existing expense."));

        Category category = checkStringCategory(request.category());

        ExpenseDTO updateExpenseDTO = ExpenseDTO.builder()
                .id(existingExpenseDTO.getId())
                .date(existingExpenseDTO.getDate())
                .category(category.toString())
                .price(request.price())
                .build();

        return Stream.of(this.expenseRepository.save(updateExpenseDTO))
                .map(newExpenseDTO -> Expense.builder()
                        .id(updateExpenseDTO.getId())
                        .category(Category.valueOf(updateExpenseDTO.getCategory()))
                        .price(updateExpenseDTO.getPrice())
                        .date(updateExpenseDTO.getDate())
                        .build()
                )
                .findFirst()
                .orElseThrow(() -> new ExpenseException("There was an exception when saving the expense."));
    }

    public void deleteExpense(UUID id) {
        ExpenseDTO existingExpenseDTO = this.expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseException("Could not find existing expense."));

        this.expenseRepository.deleteById(id);
    }

    private Category checkStringCategory(String categoryString) {
        try {
            return Category.valueOf(categoryString);
        } catch (IllegalArgumentException e) {
            return Category.OTHERS;
        }
    }
}
