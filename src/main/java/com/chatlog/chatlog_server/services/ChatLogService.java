package com.chatlog.chatlog_server.services;

import com.chatlog.chatlog_server.models.ChatLog;
import com.chatlog.chatlog_server.models.DTOs.ChatLogRequest;
import com.chatlog.chatlog_server.models.DTOs.ChatLogResponse;
import com.chatlog.chatlog_server.models.DTOs.PaginatedResponse;
import com.chatlog.chatlog_server.repository.ChatLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatLogService {

    @Autowired
    private ChatLogRepository chatLogRepository;

    public ChatLogResponse saveChatLog(ChatLogRequest chatLogRequest, String user) {

        ChatLog chatLog = new ChatLog();
        chatLog.setUser(user);
        chatLog.setMessage(chatLogRequest.getMessage());
        chatLog.setSent(chatLogRequest.isSent());

        if (chatLogRequest.getTimestamp() == null) {
            chatLog.setTimestamp(Instant.now().toEpochMilli());
        } else {
            chatLog.setTimestamp(chatLogRequest.getTimestamp());
        }
        ChatLog savedChatLog = chatLogRepository.save(chatLog);

        ChatLogResponse chatLogResponse = new ChatLogResponse();
        chatLogResponse.setId(savedChatLog.getId());
        chatLogResponse.setTimestamp(savedChatLog.getTimestamp());
        chatLogResponse.setMessage(savedChatLog.getMessage());
        chatLogResponse.setSent(savedChatLog.getSent());

        return chatLogResponse;
    }

    public PaginatedResponse getChatLogs(String user, int limit, int page) {

//        int currentPage =0;
//        long totalItems = chatLogRepository.countByUser(user);
//
//
//        if (start == null || start.isEmpty()) {
//            System.out.println("start is null");
//
//        }else {
//
//            System.out.println("start is not null");
//            ChatLog startLog = chatLogRepository.findById(start).orElseThrow(() -> new RuntimeException("Invalid start message Id: " + start));
//
//            long countFromStart = chatLogRepository.countByUserAndTimestampGreaterThanEqual(user, startLog.getTimestamp());
//           long offset = totalItems - countFromStart;
//            currentPage = (int) Math.ceil((double) offset / limit);
//            System.out.println("start is not null total item " + totalItems +" currentpage " + currentPage );
//         }

        Pageable pageable = PageRequest.of(page-1, limit);

        Page<ChatLog> chatLogPage = chatLogRepository.findByUserOrderByTimestampDesc(user, pageable);
        List<ChatLogResponse> responseDTOs = chatLogPage.getContent().stream().map(this::toResponseDTO).collect(Collectors.toList());

        int totalPages = chatLogPage.getTotalPages();

        return new PaginatedResponse(responseDTOs, totalPages, chatLogPage.getTotalElements(), chatLogPage.getNumber()+1);

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

    private ChatLogResponse toResponseDTO(ChatLog chatLog) {
        ChatLogResponse responseDTO = new ChatLogResponse();
        responseDTO.setId(chatLog.getId());
        responseDTO.setTimestamp(chatLog.getTimestamp());
        responseDTO.setMessage(chatLog.getMessage());
        responseDTO.setSent(chatLog.getSent());
        return responseDTO;
    }
}
