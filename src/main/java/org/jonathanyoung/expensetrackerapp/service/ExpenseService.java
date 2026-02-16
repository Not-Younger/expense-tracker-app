package org.jonathanyoung.expensetrackerapp.service;

import org.jonathanyoung.expensetrackerapp.Exception.ExpenseException;
import org.jonathanyoung.expensetrackerapp.model.Category;
import org.jonathanyoung.expensetrackerapp.model.DTO.ExpenseDTO;
import org.jonathanyoung.expensetrackerapp.model.DTO.UserDTO;
import org.jonathanyoung.expensetrackerapp.model.Expense;
import org.jonathanyoung.expensetrackerapp.model.request.ExpenseRequest;
import org.jonathanyoung.expensetrackerapp.repository.ExpenseRepository;
import org.jonathanyoung.expensetrackerapp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExpenseService {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    public ExpenseService(UserRepository userRepository, ExpenseRepository expenseRepository) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> findAllExpenses(Authentication authentication) {
        String username = getUsername(authentication);
        Optional<UserDTO> userDTO = userRepository.findByUsername(username);

        return userDTO.map(user -> this.expenseRepository.findAllByUser(user).stream()
                .map(expenseDTO -> Expense.builder()
                        .id(expenseDTO.getId())
                        .category(Category.valueOf(expenseDTO.getCategory()))
                        .price(expenseDTO.getPrice())
                        .date(expenseDTO.getDate())
                        .build()
                )
                .collect(Collectors.toList())).orElse(List.of());
    }

    public Optional<Expense> findExpense(UUID id, Authentication authentication) {
        String username = getUsername(authentication);
        UserDTO userDTO = userRepository.findByUsername(username).orElseThrow(() -> new ExpenseException("Authenticated user not found."));

        return this.expenseRepository.findById(id)
                .map(expenseDTO -> {
                    if (expenseDTO.getUser() != userDTO) {
                        throw new ExpenseException("Can't access that todo.");
                    }
                    return Expense.builder()
                            .id(expenseDTO.getId())
                            .category(Category.valueOf(expenseDTO.getCategory()))
                            .price(expenseDTO.getPrice())
                            .date(expenseDTO.getDate())
                            .build();
                });
    }

    public Expense addExpense(ExpenseRequest request, Authentication authentication) {
        String username = getUsername(authentication);
        UserDTO userDTO = userRepository.findByUsername(username).orElseThrow(() -> new ExpenseException("Authenticated user not found."));

        Category category = checkStringCategory(request.category());

        ExpenseDTO expenseDTO = ExpenseDTO.builder()
                .category(category.toString())
                .price(request.price())
                .date(Instant.now())
                .user(userDTO)
                .build();

        return Stream.of(this.expenseRepository.save(expenseDTO))
                .map(newExpenseDTO -> Expense.builder()
                        .id(newExpenseDTO.getId())
                        .category(Category.valueOf(newExpenseDTO.getCategory()))
                        .price(newExpenseDTO.getPrice())
                        .date(newExpenseDTO.getDate())
                        .build()
                )
                .findFirst()
                .orElseThrow(() -> new ExpenseException("There was an exception when saving the expense."));
    }

    public Expense updateExpense(UUID id, ExpenseRequest request, Authentication authentication) {
        String username = getUsername(authentication);
        UserDTO userDTO = userRepository.findByUsername(username).orElseThrow(() -> new ExpenseException("Authenticated user not found."));

        ExpenseDTO existingExpenseDTO = this.expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseException("Could not find existing expense."));

        if (existingExpenseDTO.getUser() != userDTO) {
            throw new ExpenseException("Could not update existing expense.");
        }

        Category category = checkStringCategory(request.category());

        ExpenseDTO updateExpenseDTO = ExpenseDTO.builder()
                .id(existingExpenseDTO.getId())
                .date(existingExpenseDTO.getDate())
                .category(category.toString())
                .price(request.price())
                .user(existingExpenseDTO.getUser())
                .build();

        return Stream.of(this.expenseRepository.save(updateExpenseDTO))
                .map(newExpenseDTO -> Expense.builder()
                        .id(newExpenseDTO.getId())
                        .category(Category.valueOf(newExpenseDTO.getCategory()))
                        .price(newExpenseDTO.getPrice())
                        .date(newExpenseDTO.getDate())
                        .build()
                )
                .findFirst()
                .orElseThrow(() -> new ExpenseException("There was an exception when saving the expense."));
    }

    public void deleteExpense(UUID id, Authentication authentication) {
        String username = getUsername(authentication);
        UserDTO userDTO = userRepository.findByUsername(username).orElseThrow(() -> new ExpenseException("Authenticated user not found."));

        ExpenseDTO existingExpenseDTO = this.expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseException("Could not find existing expense."));

        if (existingExpenseDTO.getUser() != userDTO) {
            throw new ExpenseException("Can't delete existing expense.");
        }

        this.expenseRepository.deleteById(id);
    }

    private Category checkStringCategory(String categoryString) {
        try {
            return Category.valueOf(categoryString);
        } catch (IllegalArgumentException e) {
            return Category.OTHERS;
        }
    }

    private String getUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
