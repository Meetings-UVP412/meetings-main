package demo.meetingsmain.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chats")
public class Chat extends BaseEntityUUID {
    @Column(name = "meeting_uuid")
    private String meetingUUID;

    private String title;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Message> messages = new ArrayList<>();

    private LocalDateTime createdAt;

    protected Chat() { }

    public Chat(String meetingUUID, String title, List<Message> messages, LocalDateTime createdAt) {
        this.meetingUUID = meetingUUID;
        this.title = title;
        this.messages = messages;
        this.createdAt = createdAt;
    }

    public String getMeetingUUID() {
        return meetingUUID;
    }

    public void setMeetingUUID(String meetingUUID) {
        this.meetingUUID = meetingUUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

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
                ", messages=" + messages +
                ", createdAt=" + createdAt +
                '}';
    }
}
