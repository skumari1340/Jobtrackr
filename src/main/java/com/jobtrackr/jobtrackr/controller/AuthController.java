package com.jobtrackr.jobtrackr.controller;

import com.jobtrackr.jobtrackr.dto.LoginRequest;
import com.jobtrackr.jobtrackr.dto.RegisterRequest;
import com.jobtrackr.jobtrackr.model.User;
import com.jobtrackr.jobtrackr.security.JwtUtil;
import com.jobtrackr.jobtrackr.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Register and Login APIs")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account with username, email and password. Password is encrypted with BCrypt."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Username or email already taken")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            return ResponseEntity.ok("User registered successfully! Welcome " + user.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
        summary = "Login and get JWT token",
        description = "Validates credentials and returns a JWT token. Use this token in the Authorize button above to access protected endpoints."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful - returns JWT token"),
        @ApiResponse(responseCode = "400", description = "Wrong username or password")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.login(request.getUsername(), request.getPassword());
            String token = jwtUtil.generateToken(user.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", user.getUsername());
            response.put("userId", user.getId());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
