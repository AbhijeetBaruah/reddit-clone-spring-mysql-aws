package com.gemini.reddit_demo.service;

import com.gemini.reddit_demo.Exception.CommentNotFoundException;
import com.gemini.reddit_demo.Exception.PostNotFoundException;
import com.gemini.reddit_demo.dto.CommentsDto;
import com.gemini.reddit_demo.mapper.CommentMapper;
import com.gemini.reddit_demo.model.Comment;
import com.gemini.reddit_demo.model.NotificationEmail;
import com.gemini.reddit_demo.model.Post;
import com.gemini.reddit_demo.model.User;
import com.gemini.reddit_demo.repository.CommentRepository;
import com.gemini.reddit_demo.repository.PostRepository;
import com.gemini.reddit_demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AuthService authService;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void createComment(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(()->new PostNotFoundException("Post Not Found Exception for post id: "+commentsDto.getPostId()));
        User currentUser = authService.getCurrentUser();
        commentRepository.save(commentMapper.mapDtoToComment(commentsDto,post,currentUser));

        User postsUser=post.getUser();
        String message = currentUser.getUsername() + " posted a comment on your post. " + post.getUrl();
        sendCommentNotification(message, postsUser);
    }

    public List<CommentsDto> getCommentByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new PostNotFoundException("Post Not Found EXception for Post id : "+postId));
        List<Comment> comments = commentRepository.findAllByPost(post)
                .orElseThrow(()->new CommentNotFoundException("No Comment Found for given Post"));
        return comments.stream()
                .map(commentMapper::mapToDto)
                .collect(toList());

    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }

    public List<CommentsDto> getCommentsByUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }
}
