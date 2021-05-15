package com.gemini.reddit_demo.repository;

import com.gemini.reddit_demo.model.Subreddit;
import com.gemini.reddit_demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit,Long> {
    Optional<Subreddit> findByName(String name);
}
