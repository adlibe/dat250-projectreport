package com.example.demo;

import com.example.demo.models.Poll;
import com.example.demo.PollManager;
import org.junit.jupiter.api.AfterEach;
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
public class PollControllerTest {

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
    public void testCreatePoll() throws Exception {
        String pollJson = "{\"id\":\"1\",\"question\":\"What is your favorite language?\"}";

        mockMvc.perform(post("/polls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pollJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.question").value("What is your favorite language?"));
    }

    @Test
    public void testGetPollById() throws Exception {
        Poll poll = new Poll();
        poll.setId(1L);
        poll.setQuestion("What is your favorite language?");
        pollManager.createPoll(poll);

        mockMvc.perform(get("/polls/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.question").value("What is your favorite language?"));
    }

    @Test
    public void testVoteOnPoll() throws Exception {
        Poll poll = new Poll();
        poll.setId(1L);
        poll.setQuestion("What is your favorite language?");
        pollManager.createPoll(poll);

        mockMvc.perform(post("/polls/1/vote")
                        .param("userId", "user1")
                        .param("option", "Java"))
                .andExpect(status().isOk());

        // Verify the vote was recorded
        mockMvc.perform(get("/polls/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.votes.user1").value("Java"));
    }

    @Test
    public void testChangeVoteOnPoll() throws Exception {
        Poll poll = new Poll();
        poll.setId(1L);
        poll.setQuestion("What is your favorite language?");
        pollManager.createPoll(poll);

        // User 1 votes for Java
        mockMvc.perform(post("/polls/1/vote")
                        .param("userId", "user1")
                        .param("option", "Java"))
                .andExpect(status().isOk());

        // User 1 changes vote to Python
        mockMvc.perform(post("/polls/1/vote")
                        .param("userId", "user1")
                        .param("option", "Python"))
                .andExpect(status().isOk());

        // Verify the vote was updated
        mockMvc.perform(get("/polls/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.votes.user1").value("Python"));
    }

    @Test
    public void testListPolls() throws Exception {
        // Create a poll first
        Poll poll = new Poll();
        poll.setId(1L);
        poll.setQuestion("What is your favorite language?");
        pollManager.createPoll(poll); // Ensure the poll is being added to the in-memory store

        // Then list polls
        mockMvc.perform(get("/polls"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))  // Expect 1 poll
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].question").value("What is your favorite language?"));
    }

    /**
    @Test
    public void testDeletePoll() throws Exception {
        // Create a poll first
        Poll poll = new Poll();
        poll.setId("1");
        poll.setQuestion("What is your favorite language?");
        pollManager.createPoll(poll);

        // Ensure the poll exists before deletion
        mockMvc.perform(get("/polls/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.question").value("What is your favorite language?"));

        // Delete the poll
        mockMvc.perform(delete("/polls/1"))
                .andExpect(status().isNoContent());  // Expecting a No Content (204) status

        // Verify the poll is no longer accessible
        mockMvc.perform(get("/polls/1"))
                .andExpect(status().isNotFound());  // Expecting Not Found (404) status
    }

    **/

    @AfterEach
    public void tearDown() {
        pollManager.clearAll();  // Clear all polls and users after each test
    }


}
