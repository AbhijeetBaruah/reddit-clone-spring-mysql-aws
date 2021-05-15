package com.gemini.reddit_demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data   //this is used to create the boilerplate(getter/setters,constructer etc
@Entity //Makes the class as Entity ==> table
@AllArgsConstructor //default constructor that takes all arguments
@NoArgsConstructor  //default constructor that takes no arguments
@Table(name = "token") //table name is given, if this is not given then class name is table name
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private Instant expiryDate;
}
