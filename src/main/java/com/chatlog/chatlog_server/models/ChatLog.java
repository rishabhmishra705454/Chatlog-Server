package com.chatlog.chatlog_server.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "chatLog")
public class ChatLog {

    @Id
    @Schema(hidden = true)
    private String id;

    @Schema(hidden = true)
    @Size(max = 15, message = "Username must be less than 16 characters")
    private String user;

    @NotBlank(message = "Message cannot be empty")
    private String message;

    @Schema(hidden = true)
    private Long timestamp;

    private Boolean sent;

    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
