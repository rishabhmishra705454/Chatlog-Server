package com.chatlog.chatlog_server.repository;

import com.chatlog.chatlog_server.models.ChatLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatLogRepository extends MongoRepository<ChatLog, String> {

    Page<ChatLog> findByUserOrderByTimestampDesc(String user, Pageable pageable);

    void deleteByUser(String user);

    boolean existsByUser(String user);
    long countByUserAndTimestampGreaterThanEqual(String user, long timestamp);

    long countByUser(String user);
}
