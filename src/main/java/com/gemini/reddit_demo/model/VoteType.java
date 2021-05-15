package com.gemini.reddit_demo.model;

import com.gemini.reddit_demo.Exception.SpringRedditException;

import java.util.Arrays;

public enum VoteType {
    UPVOTE(1),
    DOWNVOTE(0);


    VoteType(int direction) {
    }

}
