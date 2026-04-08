package com.jobtrackr.jobtrackr.service;

import com.jobtrackr.jobtrackr.dto.RegisterRequest;
import com.jobtrackr.jobtrackr.model.User;
import com.jobtrackr.jobtrackr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User register(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken!");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        // Encrypt password before saving — never save plain text!
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public User login(String username, String password) {
        // Find user by username
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found!"));

        // Check if password matches
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Wrong password!");
        }

        return user;
    }
}
