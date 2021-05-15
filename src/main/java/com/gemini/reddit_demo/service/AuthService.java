package com.gemini.reddit_demo.service;


import com.gemini.reddit_demo.Exception.SpringRedditException;
import com.gemini.reddit_demo.appConfig.AppConfig;
import com.gemini.reddit_demo.dto.AuthenticationResponse;
import com.gemini.reddit_demo.dto.LoginRequest;
import com.gemini.reddit_demo.dto.RefreshTokenRequest;
import com.gemini.reddit_demo.dto.RegisterRequest;
import com.gemini.reddit_demo.model.NotificationEmail;
import com.gemini.reddit_demo.model.User;
import com.gemini.reddit_demo.model.VerificationToken;
import com.gemini.reddit_demo.repository.UserRepository;
import com.gemini.reddit_demo.repository.VerificationTokenRepository;
import com.gemini.reddit_demo.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

//this contains main business logic to register the user i.e. creating the user and saving it to the database,sending authentication email etc
@Service
@AllArgsConstructor  //to make sure UserRepository instance is instantiated
@Slf4j
public class AuthService {
/*

    //this autowiring will give an instance of BCryptPasswordEncoder class as PasswordEncoder is defined as bean in config package
    @Autowired
    private PasswordEncoder passwordEncoder;
*/
    private final PasswordEncoder passwordEncoder; //this is better than autowiring
    private final UserRepository userRepository;//we could use autowiring but constructor injection is better than autowiring as dependency is *final* and thus thread safe.
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager; //this is interface so we have to explicitly tell what type of bean to create as there are multiple implementation of Authentication manager
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    private final AppConfig appConfig;

    @Transactional  //since we are interacting with relational database
    public void signup(RegisterRequest registerRequest) throws Exception {
        if(userRepository.findByUsername(registerRequest.getUsername()).isPresent())
        {
            throw new Exception("User name already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);//by default false

        userRepository.save(user);
        log.info("User Registered Successfully, Sending Authentication Email");
        //now generate a verification token
        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(),"Thankyou for signing up to the Spring Reddit"+
                "Please click on the following Link to complete the registration \n"+
                "http://"+appConfig.getUrl()+"/api/auth/accountVerification/"+token));
    }

    @Transactional
    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        //saving this in database
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken =verificationTokenRepository.findByToken(token);
        //verificationToken.orElseThrow(()-> new SpringRedditException("Invalid Token"));

        fetchUserAndEnable(verificationToken.orElseThrow(()->new SpringRedditException("Invalid Token")));

    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String userName = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(userName).orElseThrow(()->new SpringRedditException("User with Username "+userName+" Not Found"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {

        //this is internally calling configGlobal which is using userDetailsService interface to get the details from database (if exist) and create an authentication object
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));

        //now saving the authentication object inside the SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.generateToken(authentication);

            return AuthenticationResponse.builder()
                    .authenticationToken(token)
                    .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                    .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                    .username(loginRequest.getUsername())
                    .build();

    }
    @Transactional(readOnly = true)
    User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());//if its validated then ok otherwise it will generate exception
        //generating a jwt token if there is no exception
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());

        //now generate a AuthenticationResponse object
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .username(refreshTokenRequest.getUsername())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .build();
    }

    public void logout(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());//if its validated then ok otherwise it will generate exception
        //generating a jwt token if there is no exception
        jwtProvider.generateLogoutWithUserName(refreshTokenRequest.getUsername());
    }
}
