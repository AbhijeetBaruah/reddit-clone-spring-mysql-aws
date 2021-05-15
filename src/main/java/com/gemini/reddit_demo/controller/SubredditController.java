package com.gemini.reddit_demo.controller;

import com.gemini.reddit_demo.dto.SubredditDto;
import com.gemini.reddit_demo.model.Subreddit;
import com.gemini.reddit_demo.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {

    @Autowired
    SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto)
    {
        return ResponseEntity.status(HttpStatus.CREATED).
                body(subredditService.save2(subredditDto));
    }

    //fetch all the posts
    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddit(){
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAll());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Subreddit>> getAlllSubreddit(){
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAllSubreddit());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id)
    {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subredditService.getSubreddit(id));
    }
}
