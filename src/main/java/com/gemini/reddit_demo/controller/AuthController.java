package com.gemini.reddit_demo.controller;

import com.gemini.reddit_demo.dto.AuthenticationResponse;
import com.gemini.reddit_demo.dto.LoginRequest;
import com.gemini.reddit_demo.dto.RefreshTokenRequest;
import com.gemini.reddit_demo.dto.RegisterRequest;
import com.gemini.reddit_demo.repository.UserRepository;
import com.gemini.reddit_demo.service.AuthService;
import com.gemini.reddit_demo.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) throws Exception {

        authService.signup(registerRequest);
        return new ResponseEntity<>("User Register Successfully", HttpStatus.OK);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token)
    {
        authService.verifyAccount(token);
        return new ResponseEntity<>("User Account Activated",HttpStatus.OK);
    }

    //this giving back a JWT token and username on successful login to the client
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest)
    {
        return authService.login(loginRequest);
    }

    @PostMapping("refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest)
    {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest)
    {
        //authService.logout(refreshTokenRequest);
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token deleted Successfully");
    }

}
