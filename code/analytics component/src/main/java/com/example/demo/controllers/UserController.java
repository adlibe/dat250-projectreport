package com.example.demo.controllers;

import com.example.demo.PollManager;
import com.example.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")  // Allowing frontend to communicate with backend
public class UserController {

    @Autowired
    private PollManager pollManager;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<User> createUser(@RequestBody Map<String, String> userPayload) {
        String username = userPayload.get("username");
        String email = userPayload.get("email");

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);

        User createdUser = pollManager.createUser(user);  // Make sure the returned user object has an ID

        return ResponseEntity.status(201).body(createdUser);  // Return the user with its ID
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = pollManager.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
