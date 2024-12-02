package com.example.demo.repositories;

import com.example.demo.models.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
    // Example: Find all polls published before a specific date
    List<Poll> findByPublishedAtBefore(Instant date);
}
