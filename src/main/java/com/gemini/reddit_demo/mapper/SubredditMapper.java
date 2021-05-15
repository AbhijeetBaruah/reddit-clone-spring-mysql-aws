package com.gemini.reddit_demo.mapper;

import com.gemini.reddit_demo.dto.SubredditDto;
import com.gemini.reddit_demo.model.Post;
import com.gemini.reddit_demo.model.Subreddit;
import com.gemini.reddit_demo.model.User;
import com.gemini.reddit_demo.service.AuthService;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring") //asking the spring to treat this as Component so that it can be injected in other components
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts", expression="java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);//this method converts the subreddit to subredditDto

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    //below method is used to convert SubredditDto To subreddit
    @Mapping(target = "posts", ignore = true) //we can ignore the posts field as it should be setup while creating a new post
    @Mapping(target = "createdDate", expression = "java(getTime())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user",source = "user")
    Subreddit map(SubredditDto subreddit,User user);


    default Instant getTime(){
        return Instant.now();
    }

}