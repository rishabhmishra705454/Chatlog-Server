package com.chatlog.chatlog_server.services;

import com.chatlog.chatlog_server.models.ChatLog;
import com.chatlog.chatlog_server.repository.ChatLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ChatLogService {

    @Autowired
    private ChatLogRepository chatLogRepository;

    public ChatLog saveChatLog(ChatLog chatLog) {

        if (chatLog.getTimestamp() == null) {
            chatLog.setTimestamp(Instant.now().toEpochMilli());
        }
        ChatLog savedChatLog = chatLogRepository.save(chatLog);
        return savedChatLog;
    }

    public List<ChatLog> getChatLogs(String user, int limit, String start) {

        if (start == null || start.isEmpty()) {

            return chatLogRepository.findByUserOrderByTimestampDesc(user).stream().limit(limit).toList();

        } else {

            ChatLog startLog = chatLogRepository.findById(start).orElseThrow(() -> new RuntimeException("Invalid start message Id: " + start));

            return chatLogRepository.findByUserOrderByTimestampDesc(user).stream().dropWhile(chatLog -> !chatLog.getId().equals(startLog.getId())).skip(1).limit(limit).toList();

        }


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

}
