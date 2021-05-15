package com.gemini.reddit_demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data   //this is used to create the boilerplate(getter/setters,constructer etc
@Entity //Makes the class as Entity ==> table
@Builder //automatically produce the code required to have your class be instantiable ,usually used in constructer and methods
@AllArgsConstructor //default constructor that takes all arguments
@NoArgsConstructor  //default constructor that takes no arguments
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;
    private VoteType voteType;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId",referencedColumnName = "postId")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
}
