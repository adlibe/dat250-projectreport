package com.example.demo;

import com.example.demo.models.User;
import com.example.demo.PollManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.AfterEach;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PollManager pollManager;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testCreateUser() throws Exception {
        String userJson = "{\"id\":\"user1\",\"name\":\"John\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("user1"))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    public void testGetUsers() throws Exception {
        // Create a user first
        User user = new User();
        user.setId("user1");
        user.setName("John");
        pollManager.createUser(user); // Ensure the user is being added to the in-memory store

        // Then list users
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))  // Expect 1 user
                .andExpect(jsonPath("$[0].id").value("user1"))
                .andExpect(jsonPath("$[0].name").value("John"));
    }


    @Test
    public void testCreateAndListUsers() throws Exception {
        String userJson1 = "{\"id\":\"user1\",\"name\":\"John\"}";
        String userJson2 = "{\"id\":\"user2\",\"name\":\"Doe\"}";

        // Create first user
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(status().isCreated());

        // Create second user
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andExpect(status().isCreated());

        // List all users
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))  // Expect 2 users
                .andExpect(jsonPath("$[0].id").value("user1"))
                .andExpect(jsonPath("$[1].id").value("user2"));
    }
    @AfterEach
    public void tearDown() {
        pollManager.clearAll();  // Clear all polls and users after each test
    }

}
