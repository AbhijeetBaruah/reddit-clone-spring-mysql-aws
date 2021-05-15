package com.gemini.reddit_demo.repository;

import com.gemini.reddit_demo.model.Comment;
import com.gemini.reddit_demo.model.Post;
import com.gemini.reddit_demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    Optional<List<Comment>> findAllByPost(Post post);
    List<Comment> findAllByUser(User user);
    Optional<Integer> countByPost(Post post);
}
