package com.chatlog.chatlog_server.repository;

import com.chatlog.chatlog_server.models.ChatLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatLogRepository extends MongoRepository<ChatLog, String> {

    List<ChatLog> findByUserOrderByTimestampDesc(String user);

    void deleteByUser(String user);

    boolean existsByUser(String user);
}
