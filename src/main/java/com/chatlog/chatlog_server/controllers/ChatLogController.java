package com.chatlog.chatlog_server.controllers;


import com.chatlog.chatlog_server.models.DTOs.ChatLogRequest;
import com.chatlog.chatlog_server.models.DTOs.ChatLogResponse;
import com.chatlog.chatlog_server.models.DTOs.PaginatedResponse;
import com.chatlog.chatlog_server.services.ChatLogService;
import com.chatlog.chatlog_server.utils.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatLogController {

    @Autowired
    private ChatLogService chatLogService;

    @Operation(summary = "Timestamp is added automatically , But you can pass own ")
    @PostMapping("/chatlogs")
    public ResponseEntity<Object> saveChatLog( @Valid @RequestBody ChatLogRequest chatLogRequest, Authentication authentication) {

        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            ChatLogResponse savedLog = chatLogService.saveChatLog(chatLogRequest,userDetails.getUsername());
            return ResponseHandler.generateResponse("Chat log of "+userDetails.getUsername()+" saved successfully", HttpStatus.CREATED, savedLog);

        } catch (Exception e) {

            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


    @GetMapping("/chatlogs")
    public ResponseEntity<Object> getChatLogs(@RequestParam(value = "limit", defaultValue = "10") int limit, @RequestParam(value = "start", required = false) String start , Authentication authentication) {

        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

           PaginatedResponse paginatedResponse = chatLogService.getChatLogs(userDetails.getUsername(), limit, start);

            return ResponseHandler.generateResponse("Chat log of "+userDetails.getUsername()+" fetched successfully", HttpStatus.OK, paginatedResponse);

        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/chatlogs")
    public ResponseEntity<Object> deleteUserChatLogs(Authentication authentication) {

        try {

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            boolean userExists = chatLogService.checkIfUserHasChatLogs(userDetails.getUsername());
            if (!userExists) {
                return ResponseHandler.generateResponse("User '" + userDetails.getUsername() + "' does not have any chatlogs", HttpStatus.NOT_FOUND, null);
            }

            chatLogService.deleteAllChatLogForUser(userDetails.getUsername());
            return ResponseHandler.generateResponse("All chatlog for user '" + userDetails.getUsername() + "' deleted successfully", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


    @DeleteMapping("/chatlogs/{msgid}")
    public ResponseEntity<Object> deleteChatLogById( @PathVariable("msgid") String msgId , Authentication authentication) {

        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            int result = chatLogService.deleteChatLogById(userDetails.getUsername(), msgId);
            if (result == 0) {
                return ResponseHandler.generateResponse("Chatlog with id '" + msgId + "' does not exist", HttpStatus.NOT_FOUND, null);
            } else if (result == -1) {
                return ResponseHandler.generateResponse(
                        "Chat log with ID '" + msgId + "' does not belong to user '" + userDetails.getUsername() + "'.",
                        HttpStatus.FORBIDDEN,
                        null
                );
            } else {
                return ResponseHandler.generateResponse("Chatlog with id '" + msgId + "' deleted successfully", HttpStatus.OK, null);
            }


        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);

        }

    }
}
