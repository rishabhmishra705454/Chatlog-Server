package com.chatlog.chatlog_server.utils;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ChatLog Server API",
                version = "1.0.0",
                description = "API for managing user chat logs. This includes creating, retrieving, updating, and deleting chat logs."

        )
)
public class OpenAPIConfig {
}