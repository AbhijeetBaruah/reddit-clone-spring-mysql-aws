package com.gemini.reddit_demo.repository;


import com.gemini.reddit_demo.dto.PostResponse;
import com.gemini.reddit_demo.model.Post;
import com.gemini.reddit_demo.model.Subreddit;
import com.gemini.reddit_demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    Optional<List<Post>> findAllBySubreddit(Subreddit subreddit);

    List<Post> findAllByUser(User user);

    List<Post> findByUser(User user);

    @Query(value = "SELECT vote_count FROM post WHERE post.post_id=:postId",nativeQuery = true)
    Optional<Integer> getCountOfVote(Long postId);
    //Post is the Post entity class and Long represents the primary key data type

}
