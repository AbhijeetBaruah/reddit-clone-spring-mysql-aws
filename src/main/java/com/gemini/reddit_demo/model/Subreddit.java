package com.gemini.reddit_demo.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

@Data   //this is used to create the boilerplate(getter/setters,constructer etc
@Entity //Makes the class as Entity ==> table
@Builder(toBuilder = true) //automatically produce the code required to have your class be instantiable ,usually used in constructer and methods
@AllArgsConstructor //default constructor that takes all arguments
@NoArgsConstructor  //default constructor that takes no arguments
public class Subreddit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Community name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Post> posts;

    private Instant createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

}
