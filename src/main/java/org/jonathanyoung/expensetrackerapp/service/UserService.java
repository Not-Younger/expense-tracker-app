package org.jonathanyoung.expensetrackerapp.service;

import lombok.extern.slf4j.Slf4j;
import org.jonathanyoung.expensetrackerapp.Exception.ExpenseException;
import org.jonathanyoung.expensetrackerapp.model.DTO.UserDTO;
import org.jonathanyoung.expensetrackerapp.model.request.UserRequest;
import org.jonathanyoung.expensetrackerapp.model.response.UserResponse;
import org.jonathanyoung.expensetrackerapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(UserRequest request) {
        if (this.userRepository.findByUsername(request.username()).isPresent()) {
            throw new ExpenseException("User with username: " + request.username() + " already exists...");
        }

        UserDTO registeredUserDTO = this.userRepository.save(UserDTO.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .authorities("")
                .build());

        // Generate token

        return UserResponse.builder()
                .username(registeredUserDTO.getUsername())
                .token("token")
                .build();
    }

    public UserResponse login(UserRequest request) {
        UserDTO registeredUserDTO = this.userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ExpenseException("User not found. Please register first."));

        if (!passwordEncoder.matches(request.password(), registeredUserDTO.getPassword())) {
            throw new ExpenseException("Invalid credentials.");
        }

        // Generate token

        return UserResponse.builder()
                .username(registeredUserDTO.getUsername())
                .token("token")
                .build();
    }
}
