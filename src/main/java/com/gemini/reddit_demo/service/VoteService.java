package com.gemini.reddit_demo.service;

import com.gemini.reddit_demo.Exception.PostNotFoundException;
import com.gemini.reddit_demo.Exception.SpringRedditException;
import com.gemini.reddit_demo.dto.VoteDto;
import com.gemini.reddit_demo.model.Post;
import com.gemini.reddit_demo.model.Vote;
import com.gemini.reddit_demo.model.VoteType;
import com.gemini.reddit_demo.repository.PostRepository;
import com.gemini.reddit_demo.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class VoteService {
    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final AuthService authService;

    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(()->new PostNotFoundException("Post not found Exception"));

        //this is used to fetch the vote on a particular post if previously done by the current user
        Optional<Vote> voteByPostAndUser =voteRepository.findByPostAndUser(post,authService.getCurrentUser());

        //if the vote is present and voteType is equal to current voteType then error
        if(voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType()))
        {
            throw new SpringRedditException("You have already "+voteDto.getVoteType()+"'d for this Post");
        }


        if(VoteType.UPVOTE.equals(voteDto.getVoteType()))
        {
            log.info("Inside Upvote");
            if((post.getVoteCount()==null || post.getVoteCount()==0) && !voteByPostAndUser.isPresent()){
                log.info("If block UPVOTE");
                post.setVoteCount(1);
            }else{
                log.info("Else block UPVOTE");
                //now check if the user has previously downvoted it so increment it By 2
                                        // reason , total_vote = 0;
                                        // downvoted, total_vote => 0-1=-1;
                                        //upvoted total_vote =>-1+1=0 ,wrong so total_vote => -1+2=1
                if(voteByPostAndUser.isPresent() && !voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())){
                    post.setVoteCount(post.getVoteCount()+2);
                }else {
                    post.setVoteCount(post.getVoteCount() + 1);
                }
            }
        } else {
            log.info("Inside Downvote");
            if((post.getVoteCount()==null || post.getVoteCount()==0) && !voteByPostAndUser.isPresent()){
                log.info("IF block Downvote");
                post.setVoteCount(-1);
            }else{
                log.info("Else block Downvote");
                //reason stated above
                if(voteByPostAndUser.isPresent() && !voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())){
                    post.setVoteCount(post.getVoteCount()-2);
                }else {
                    post.setVoteCount(post.getVoteCount() - 1);
                }
            }
        }
        if(voteByPostAndUser.isPresent()){
            Long id = voteByPostAndUser.get().getVoteId();
            Vote vote = mapToVote(voteDto, post);
            vote.setVoteId(id);
            voteRepository.save(vote);
        }else{
        voteRepository.save(mapToVote(voteDto,post));}
        postRepository.save(post);
    }

    //mapper
    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
