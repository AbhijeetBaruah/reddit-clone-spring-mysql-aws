package com.gemini.reddit_demo.mapper;

import com.gemini.reddit_demo.dto.CommentsDto;
import com.gemini.reddit_demo.model.Comment;
import com.gemini.reddit_demo.model.Post;
import com.gemini.reddit_demo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CommentMapper {


    @Mapping(target = "text", source = "commentsDto.text")
    @Mapping(target ="createdDate",expression = "java(java.time.Instant.now())")
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user",source = "user")
    Comment mapDtoToComment(CommentsDto commentsDto, Post post, User user);

    @Mapping(target = "userName" , expression = "java(comment.getUser().getUsername())")
    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    CommentsDto mapToDto(Comment comment);
}
