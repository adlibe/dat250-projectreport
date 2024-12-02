package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // Register a new user
    public User registerUser(User user) {
        // Set default role
        user.setRoles(List.of("ROLE_USER"));

        // Save the user to the database
        User savedUser = userRepository.save(user);

        // Generate a JWT token for the new user
        String token = jwtUtil.generateToken(savedUser.getUsername());
        savedUser.setToken(token);

        // Save the token back to the user
        userRepository.save(savedUser);

        return savedUser;
    }

    // Validate token and return the user if valid
    public User validateTokenAndGetUser(String token) {
        // Extract username from the token
        String username = jwtUtil.extractUsername(token);

        // Find the user by username
        Optional<User> userOptional = userRepository.findByUsername(username);

        // Validate the token against the stored user information
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (jwtUtil.validateToken(token, user.getUsername())) {
                return user; // Return the user if the token is valid
            }
        }

        return null; // Return null if the token is invalid
    }

    // Check if a username already exists
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // Save a user (general-purpose method)
    public void saveUser(User user) {
        userRepository.save(user);
    }

    // Authenticate a user by email (or username) and password
    public Optional<User> authenticate(String emailOrUsername, String password) {
        Optional<User> userOptional = userRepository.findByEmail(emailOrUsername);
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByUsername(emailOrUsername);
        }

        return userOptional; // Simplified authentication for JWT-based flow
    }

    // Get all users (example method)
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete all users
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}
