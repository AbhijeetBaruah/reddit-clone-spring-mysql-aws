package com.gemini.reddit_demo.service;

import com.gemini.reddit_demo.Exception.SpringRedditException;
import com.gemini.reddit_demo.Exception.SubredditNotFoundException;
import com.gemini.reddit_demo.dto.SubredditDto;
import com.gemini.reddit_demo.mapper.SubredditMapper;
import com.gemini.reddit_demo.model.Subreddit;
import com.gemini.reddit_demo.model.User;
import com.gemini.reddit_demo.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@Slf4j
@AllArgsConstructor
public class SubredditService {
    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;
    private final AuthService authService;

    @Transactional (readOnly = true)
    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(()->new SubredditNotFoundException("No Subreddit found with Id : "+id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }


    @Transactional
    public SubredditDto save2(SubredditDto subredditDto){
        User currentUser = authService.getCurrentUser();
        if(subredditRepository.findByName(subredditDto.getName()).isPresent()){
            throw new SpringRedditException("Already a subreddit Exist with same name");
        }
        Subreddit subreddit = subredditMapper.map(subredditDto,currentUser);
        Subreddit save = subredditRepository.save(subreddit);
        subredditDto.setId(save.getId());
        return subredditDto;
    }

    @Transactional (readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(toList());
    }

    public List<Subreddit> getAllSubreddit() {
        return subredditRepository.findAll();
    }
}
