package com.gemini.reddit_demo.repository;

import com.gemini.reddit_demo.model.Post;
import com.gemini.reddit_demo.model.User;
import com.gemini.reddit_demo.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
    Optional<Vote> findByPostAndUser(Post post, User user);
    Optional<Integer> countByPost(Post post);


    @Query(value = "SELECT SUM(vote.vote_type) FROM vote WHERE vote.post_id=:id",nativeQuery = true)
    Optional<Integer>getCountOfVote(@Param("id") Long id);
}
