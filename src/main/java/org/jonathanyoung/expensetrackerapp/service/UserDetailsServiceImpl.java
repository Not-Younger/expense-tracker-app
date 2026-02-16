package org.jonathanyoung.expensetrackerapp.service;

import org.jonathanyoung.expensetrackerapp.model.DTO.UserDTO;
import org.jonathanyoung.expensetrackerapp.model.DTO.UserDetailsImpl;
import org.jonathanyoung.expensetrackerapp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDto = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found."));

        return new UserDetailsImpl(userDto);
    }
}

