package com.chatlog.chatlog_server.services;

import com.chatlog.chatlog_server.models.ChatLog;
import com.chatlog.chatlog_server.models.DTOs.ChatLogRequestDTO;
import com.chatlog.chatlog_server.models.DTOs.ChatLogResponseDTO;
import com.chatlog.chatlog_server.repository.ChatLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatLogService {

    @Autowired
    private ChatLogRepository chatLogRepository;

    public ChatLogResponseDTO saveChatLog(ChatLogRequestDTO chatLogRequestDTO, String user) {

        ChatLog chatLog = new ChatLog();
        chatLog.setUser(user);
        chatLog.setMessage(chatLogRequestDTO.getMessage());
        chatLog.setSent(chatLogRequestDTO.isSent());

        if (chatLogRequestDTO.getTimestamp() == null) {
            chatLog.setTimestamp(Instant.now().toEpochMilli());
        }else {
            chatLog.setTimestamp(chatLogRequestDTO.getTimestamp());
        }
        ChatLog savedChatLog = chatLogRepository.save(chatLog);

        ChatLogResponseDTO chatLogResponseDTO = new ChatLogResponseDTO();
        chatLogResponseDTO.setId(savedChatLog.getId());
        chatLogResponseDTO.setTimestamp(savedChatLog.getTimestamp());
        chatLogResponseDTO.setMessage(savedChatLog.getMessage());
        chatLogResponseDTO.setSent(savedChatLog.getSent());

        return chatLogResponseDTO;
    }

    public List<ChatLogResponseDTO> getChatLogs(String user, int limit, String start) {

        List<ChatLog> chatLogs;

        if (start == null || start.isEmpty()) {

           chatLogs = chatLogRepository.findByUserOrderByTimestampDesc(user).stream().limit(limit).toList();

        } else {

            ChatLog startLog = chatLogRepository.findById(start).orElseThrow(() -> new RuntimeException("Invalid start message Id: " + start));

            chatLogs = chatLogRepository.findByUserOrderByTimestampDesc(user).stream().dropWhile(chatLog -> !chatLog.getId().equals(startLog.getId()))
                    .skip(1)
                    .limit(limit)
                    .toList();
        }

        return chatLogs.stream().map(this::toResponseDTO).collect(Collectors.toList());


    }

    public void deleteAllChatLogForUser(String user) {

        chatLogRepository.deleteByUser(user);
    }

    public boolean checkIfUserHasChatLogs(String user) {

        return chatLogRepository.existsByUser(user);
    }

    public int deleteChatLogById(String user, String msgId) {

        return chatLogRepository.findById(msgId).map(chatLog -> {
            if (!chatLog.getUser().equals(user)) {
                return -1;
            }
            chatLogRepository.deleteById(msgId);
            return 1;
        }).orElse(0);
    }

    private ChatLogResponseDTO toResponseDTO(ChatLog chatLog) {
        ChatLogResponseDTO responseDTO = new ChatLogResponseDTO();
        responseDTO.setId(chatLog.getId());
        responseDTO.setTimestamp(chatLog.getTimestamp());
        responseDTO.setMessage(chatLog.getMessage());
        responseDTO.setSent(chatLog.getSent());
        return responseDTO;
    }
}
