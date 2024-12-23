package com.chatlog.chatlog_server.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.chatlog.chatlog_server.utils.ResponseHandler;

import java.io.IOException;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        logger.error("Unauthorized error: {}", authException.getMessage());

        ResponseEntity<Object> errorResponse = ResponseHandler.generateResponse(
                "Unauthorized error: "+ authException.getMessage() , HttpStatus.UNAUTHORIZED, null);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(
                new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(errorResponse.getBody())
        );
    }
}