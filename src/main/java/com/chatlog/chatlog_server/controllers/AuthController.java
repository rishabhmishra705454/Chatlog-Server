package com.chatlog.chatlog_server.controllers;

import com.chatlog.chatlog_server.models.DTOs.JwtResponse;
import com.chatlog.chatlog_server.models.DTOs.LoginRequest;
import com.chatlog.chatlog_server.models.DTOs.SignupRequest;
import com.chatlog.chatlog_server.repository.UserRepository;
import com.chatlog.chatlog_server.security.jwt.JwtUtils;
import com.chatlog.chatlog_server.security.services.UserDetailsImpl;
import com.chatlog.chatlog_server.services.AuthService;
import com.chatlog.chatlog_server.utils.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;



    @Autowired
    AuthService authService;


    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getUsername(), userDetails.getEmail(), userDetails.getId());
        return ResponseHandler.generateResponse("", HttpStatus.OK, jwtResponse);

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (authService.isUserNameExists(signupRequest.getUsername())) {
            return ResponseHandler.generateResponse("Username is already taken!", HttpStatus.BAD_REQUEST, null);
        }

        if (authService.isEmailExists(signupRequest.getEmail())){
            return ResponseHandler.generateResponse("Email is already taken!", HttpStatus.BAD_REQUEST, null);

        }

        String msg = authService.saveUserData(signupRequest);
        return ResponseHandler.generateResponse(msg, HttpStatus.CREATED, null);
    }
}
