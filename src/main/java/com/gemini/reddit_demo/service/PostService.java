package com.gemini.reddit_demo.service;

import com.gemini.reddit_demo.Exception.PostNotFoundException;
import com.gemini.reddit_demo.Exception.SpringRedditException;
import com.gemini.reddit_demo.Exception.SubredditNotFoundException;
import com.gemini.reddit_demo.dto.PostRequest;
import com.gemini.reddit_demo.dto.PostResponse;
import com.gemini.reddit_demo.mapper.PostMapper;
import com.gemini.reddit_demo.model.Post;
import com.gemini.reddit_demo.model.Subreddit;
import com.gemini.reddit_demo.model.User;
import com.gemini.reddit_demo.repository.PostRepository;
import com.gemini.reddit_demo.repository.SubredditRepository;
import com.gemini.reddit_demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final UserRepository userRepository;
    
    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(()->new SpringRedditException("No Subreddit found by name "+postRequest.getSubredditName()));
        User currentUser = authService.getCurrentUser();
        //mapping postrequest to post entity
        Post post = postRepository.save(postMapper.map(postRequest,subreddit,currentUser));
        subreddit.getPosts().add(post);
        subredditRepository.save(subreddit);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(()->new SubredditNotFoundException("No Subreddit found with Id "+subredditId));

        List<Post> posts = postRepository.findAllBySubreddit(subreddit)
                .orElseThrow(()->new PostNotFoundException("No Posts Found linked to Subreddit Id "+subredditId));
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new SpringRedditException("User Not Found"));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }
}
