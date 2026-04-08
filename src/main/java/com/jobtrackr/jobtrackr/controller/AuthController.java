package com.jobtrackr.jobtrackr.controller;

import com.jobtrackr.jobtrackr.dto.LoginRequest;
import com.jobtrackr.jobtrackr.dto.RegisterRequest;
import com.jobtrackr.jobtrackr.model.User;
import com.jobtrackr.jobtrackr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            return ResponseEntity.ok("User registered successfully! Welcome " + user.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok("Login successful! Welcome back " + user.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
