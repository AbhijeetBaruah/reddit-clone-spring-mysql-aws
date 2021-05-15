package com.gemini.reddit_demo.Exception;

public class SubredditNotFoundException extends RuntimeException{
    public SubredditNotFoundException(String exMessage){
        super(exMessage);
    }
}
