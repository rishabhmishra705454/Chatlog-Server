package com.chatlog.chatlog_server.controllers;


import com.chatlog.chatlog_server.models.ChatLog;
import com.chatlog.chatlog_server.models.DTOs.ChatLogRequestDTO;
import com.chatlog.chatlog_server.models.DTOs.ChatLogResponseDTO;
import com.chatlog.chatlog_server.services.ChatLogService;
import com.chatlog.chatlog_server.utils.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatlogs")
public class ChatLogController {

    @Autowired
    private ChatLogService chatLogService;

    @Operation(summary = "Timestamp is added automatically , But you can pass own ")
    @PostMapping("/{user}")
    public ResponseEntity<Object> saveChatLog(@PathVariable("user") String user, @Valid @RequestBody ChatLogRequestDTO chatLogRequestDTO) {

        try {
            ChatLogResponseDTO savedLog = chatLogService.saveChatLog(chatLogRequestDTO,user);
            return ResponseHandler.generateResponse("Chat log saved successfully", HttpStatus.CREATED, savedLog);

        } catch (Exception e) {

            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


    @GetMapping("/{user}")
    public ResponseEntity<Object> getChatLogs(@PathVariable("user") String user, @RequestParam(value = "limit", defaultValue = "10") int limit, @RequestParam(value = "start", required = false) String start) {

        try {
            List<ChatLogResponseDTO> chatLogs = chatLogService.getChatLogs(user, limit, start);

            return ResponseHandler.generateResponse("Chat logs fetched successfully", HttpStatus.OK, chatLogs);

        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/{user}")
    public ResponseEntity<Object> deleteUserChatLogs(@PathVariable("user") String user) {

        try {

            boolean userExists = chatLogService.checkIfUserHasChatLogs(user);
            if (!userExists) {
                return ResponseHandler.generateResponse("User '" + user + "' does not have any chatlogs", HttpStatus.NOT_FOUND, null);
            }

            chatLogService.deleteAllChatLogForUser(user);
            return ResponseHandler.generateResponse("All chatlog for user '" + user + "' deleted successfully", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


    @DeleteMapping("/{user}/{msgid}")
    public ResponseEntity<Object> deleteChatLogById(@PathVariable("user") String user, @PathVariable("msgid") String msgId) {

        try {
            int result = chatLogService.deleteChatLogById(user, msgId);
            if (result == 0) {
                return ResponseHandler.generateResponse("Chatlog with id '" + msgId + "' does not exist", HttpStatus.NOT_FOUND, null);
            } else if (result == -1) {
                return ResponseHandler.generateResponse(
                        "Chat log with ID '" + msgId + "' does not belong to user '" + user + "'.",
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
