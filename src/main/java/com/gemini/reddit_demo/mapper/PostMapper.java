package com.gemini.reddit_demo.mapper;

import com.gemini.reddit_demo.dto.PostRequest;
import com.gemini.reddit_demo.dto.PostResponse;
import com.gemini.reddit_demo.model.Post;
import com.gemini.reddit_demo.model.Subreddit;
import com.gemini.reddit_demo.model.User;
import com.gemini.reddit_demo.repository.CommentRepository;
import com.gemini.reddit_demo.repository.PostRepository;
import com.gemini.reddit_demo.repository.VoteRepository;
import com.gemini.reddit_demo.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private PostRepository postRepository;

    //mapping from postRequest To Post Entity
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "voteCount", constant = "0")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "postName", source = "postName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "voteCount",expression = "java(getVoteCount(post))")
    public abstract PostResponse mapToDto(Post post);


    Integer commentCount(Post post) {
        return commentRepository.countByPost(post).orElse(0);
    }

    Integer getVoteCount(Post post){

        return postRepository.getCountOfVote(post.getPostId()).orElse(0);
    }
    String getDuration(Post post) {
        return new PrettyTime().format(post.getCreatedDate());
    }

}
