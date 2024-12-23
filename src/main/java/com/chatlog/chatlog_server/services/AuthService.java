package com.chatlog.chatlog_server.services;

import com.chatlog.chatlog_server.models.DTOs.JwtResponse;
import com.chatlog.chatlog_server.models.DTOs.SignupRequest;
import com.chatlog.chatlog_server.models.User;
import com.chatlog.chatlog_server.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isUserNameExists(String username) {
        return userRepository.existsByUsername(username);
    }


    public String saveUserData(@Valid SignupRequest signupRequest) {

        User user = new User(signupRequest.getUsername(), encoder.encode(signupRequest.getPassword()) , signupRequest.getEmail());
        userRepository.save(user);
        return "User registered successfully!";

    }
}
