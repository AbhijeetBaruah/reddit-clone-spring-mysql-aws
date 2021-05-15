package com.gemini.reddit_demo.model;


import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data   //this is used to create the boilerplate(getter/setters,constructer etc
@Entity //Makes the class as Entity ==> table
@Builder //automatically produce the code required to have your class be instantiable ,usually used in constructer and methods
@AllArgsConstructor //default constructor that takes all arguments
@NoArgsConstructor  //default constructor that takes no arguments
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @NotBlank(message = "Post name cannot be empty or null")
    private String postName;

    @Nullable
    private String url;

    @Nullable
    @Lob //annotated field should be represented as BLOB (binary data) in the DataBase
    private String description;
    private  Integer voteCount;

    @ManyToOne(fetch = FetchType.LAZY) //This Post entity has many to one relationship with User entity, also LAZY so that on demand loading
    @JoinColumn(name = "userId", referencedColumnName = "userId") //this will create a join using a foreign key which is pointing to userId of User Entity
    private  User user;
    private Instant createdDate; ///this is storing the instant time when the post is created
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Subreddit subreddit;
}
