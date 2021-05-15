package com.gemini.reddit_demo.Exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String exMessage) {
        super(exMessage);
    }
}
