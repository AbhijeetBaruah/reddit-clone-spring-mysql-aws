package com.gemini.reddit_demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@Data   //this is used to create the boilerplate(getter/setters,constructer etc
@Entity //Makes the class as Entity ==> table
@Builder //automatically produce the code required to have your class be instantiable ,usually used in constructer and methods
@AllArgsConstructor //default constructor that takes all arguments
@NoArgsConstructor  //default constructor that takes no arguments
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId",referencedColumnName = "postId")
    private Post post;
    private Instant createdDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",referencedColumnName = "userId")
    private User user;
}


