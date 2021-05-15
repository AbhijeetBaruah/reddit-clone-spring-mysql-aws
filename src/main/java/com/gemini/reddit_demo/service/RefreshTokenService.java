package com.gemini.reddit_demo.service;

import com.gemini.reddit_demo.Exception.SpringRedditException;
import com.gemini.reddit_demo.model.RefreshToken;
import com.gemini.reddit_demo.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;


//this service is responsible for creation, deletion and validating the token
//this service will be used by authService class for refreshToken operations
@Service
@Transactional
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    public void validateRefreshToken(String token){
        refreshTokenRepository.findByToken(token)
                .orElseThrow(()->new SpringRedditException("Invalid refresh Token"));

    }

    public void deleteRefreshToken(String token)
    {
        refreshTokenRepository.deleteByToken(token);
    }
}
