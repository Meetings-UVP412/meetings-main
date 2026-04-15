package demo.meetingsmain.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.persistence.*;
import jakarta.persistence.*;

@Entity
@Table(name = "chats")
public class Chat extends BaseEntityUUID {
    private String meetingUUID;
    private String title;
    private String messagesJson;
    private LocalDateTime createdAt;

    protected Chat() {}

    public Chat(String meetingUUID, String title, List<Message> initialMessages, LocalDateTime createdAt) {
        this.meetingUUID = meetingUUID;
        this.title = title;
        this.setMessages(initialMessages);
        this.createdAt = createdAt;
    }

    public List<Message> readMessages() {
        if (messagesJson == null || messagesJson.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return Arrays.asList(mapper.readValue(messagesJson, Message[].class));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse messages JSON", e);
        }
    }

    public void setMessages(List<Message> messages) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.messagesJson = mapper.writeValueAsString(messages != null ? messages : List.of());
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize messages to JSON", e);
        }
    }

    @Column(name = "meeting_uuid", nullable = false)
    public String getMeetingUUID() {
        return meetingUUID;
    }

    public void setMeetingUUID(String meetingUUID) {
        this.meetingUUID = meetingUUID;
    }

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(columnDefinition = "jsonb", length = 2047)
    @JdbcTypeCode(SqlTypes.JSON)
    public String getMessagesJson() {
        return messagesJson;
    }

    public void setMessagesJson(String messagesJson) {
        this.messagesJson = messagesJson;
    }

    @Column(name = "created_at", nullable = false)
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "meetingUUID='" + meetingUUID + '\'' +
                ", title='" + title + '\'' +
                ", messagesJson='" + messagesJson + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}